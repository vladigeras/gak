package ru.iate.gak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.TestsConfig;
import ru.iate.gak.domain.Commission;
import ru.iate.gak.domain.Role;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.model.UserEntity;
import ru.iate.gak.repository.CommissionRepository;
import ru.iate.gak.repository.UserRepository;
import ru.iate.gak.service.impl.CommissionServiceImpl;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        TestsConfig.class,
        CommissionServiceImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommissionServiceImplTests {

    @MockBean
    private TaskScheduler scheduler;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void whenGetCommissionByListId_thenReturnArrayOfCommissionFromCurrentListId() {
        //given
        Integer listId = 1;
        Integer subListId = 2;

        UserEntity user = new UserEntity();
        user.setId(1L);
        userRepository.save(user);

        CommissionEntity commission1 = new CommissionEntity();
        commission1.setUser(user);
        commission1.setListId(listId);
        commissionRepository.save(commission1);

        CommissionEntity commission2 = new CommissionEntity();
        commission2.setListId(subListId);
        commissionRepository.save(commission2);

        //when
        List<Commission> found = commissionService.getCommissionsByListId(listId);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getListId(), listId);
    }

    @Test
    @Transactional
    public void whenSetPresidentRoleByCommission_thenAddPresidentRole() {
        //given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setRoles(Set.of());
        userRepository.save(user);

        CommissionEntity commission = new CommissionEntity();
        commission.setUser(user);
        commission.setId(1L);
        commissionRepository.save(commission);

        //when
        commissionService.setPresidentRoleTemporally(new Commission(commission));
        CommissionEntity found = commissionRepository.getByUser(user);

        //then
        assertTrue(found.getUser().getRoles().contains(Role.PRESIDENT));
    }
}
