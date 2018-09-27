package ru.iate.gak.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import ru.iate.gak.dto.CommissionDto;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.model.Role;
import ru.iate.gak.repository.CommissionRepository;
import ru.iate.gak.service.CommissionService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class CommissionServiceImpl implements CommissionService {

    private static final Logger logger = LoggerFactory.getLogger(CommissionServiceImpl.class);

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public List<CommissionEntity> getCommissionsByListId(Integer listId) {
        return commissionRepository.getCommissionEntitiesByListId(listId);
    }

    @Override
    @Transactional
    public void setPresidentRoleTemporally(CommissionDto commission) {
        CommissionEntity commissionEntity = commissionRepository.findById(commission.id)
                .orElseThrow(() -> new RuntimeException("Член комиссии с id = " + commission.id + " не найден"));
        if (commissionEntity.getUser() == null) throw new RuntimeException("Произошла ошибка");
        if (commissionEntity.getUser().getRoles().contains(Role.PRESIDENT)) throw new RuntimeException("Данный член комиссии уже является ПРЕДСЕДАТЕЛЕМ");

        commissionEntity.getUser().getRoles().add(Role.PRESIDENT);

        commissionRepository.save(commissionEntity);

        logger.info("Члену комиссии " + commissionEntity.getUser().getLogin() + " временно передали роль ПРЕДСЕДАТЕЛЯ");
    }

    @Override
    public void doTaskForDeleteRoleWhenDateExpired(CommissionDto commission, Date date) {
        taskScheduler.schedule(() -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    CommissionEntity commissionEntity = commissionRepository.findById(commission.id)
                            .orElseThrow(() -> new RuntimeException("Член комиссии с id = " + commission.id + " не найден"));
                    if (commissionEntity == null) throw new RuntimeException("Произошла ошибка");

                    if (commissionEntity.getUser() == null) throw new RuntimeException("Произошла ошибка");
                    commissionEntity.getUser().getRoles().remove(Role.PRESIDENT);
                    commissionRepository.save(commissionEntity);

                    logger.info("У члена комиссии " + commissionEntity.getUser().getLogin() + " забрали временную роль ПРЕДСЕДАТЕЛЯ");
                }
            });
        }, date);
        }
    }
