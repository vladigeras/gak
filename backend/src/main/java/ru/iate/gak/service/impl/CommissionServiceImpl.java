package ru.iate.gak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.iate.gak.domain.Commission;
import ru.iate.gak.domain.Role;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.repository.CommissionRepository;
import ru.iate.gak.service.CommissionService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommissionServiceImpl implements CommissionService {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public List<Commission> getCommissionsByListId(Integer listId) {
        return commissionRepository.getCommissionEntitiesByListId(listId).stream().map(Commission::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void setPresidentRoleTemporally(Commission commission) {
        CommissionEntity commissionEntity = commissionRepository.findOne(commission.getId());
        if (commissionEntity == null) throw new RuntimeException("Произошла ошибка");

        if (commissionEntity.getUser() == null) throw new RuntimeException("Произошла ошибка");
        commissionEntity.getUser().getRoles().add(Role.PRESIDENT);

        commissionRepository.save(commissionEntity);
    }

    @Override
    public void doTaskForDeleteRoleWhenDateExpired(Commission commission, Date date) {
        taskScheduler.schedule(() -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    CommissionEntity commissionEntity = commissionRepository.findOne(commission.getId());
                    if (commissionEntity == null) throw new RuntimeException("Произошла ошибка");

                    if (commissionEntity.getUser() == null) throw new RuntimeException("Произошла ошибка");
                    commissionEntity.getUser().getRoles().remove(Role.PRESIDENT);
                    commissionRepository.save(commissionEntity);
                }
            });
        }, date);
        }
    }
