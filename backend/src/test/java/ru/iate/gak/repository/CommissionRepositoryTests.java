package ru.iate.gak.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.model.UserEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommissionRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommissionRepository commissionRepository;

    @Test
    public void whenGetCommissionByUser_thenReturnCommissionLinkedWithUser() {
        //given
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();

        CommissionEntity commission1 = new CommissionEntity();
        commission1.setUser(user1);
        CommissionEntity commission2 = new CommissionEntity();
        commission2.setUser(user2);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(commission1);
        entityManager.persist(commission2);
        entityManager.flush();

        //when
        CommissionEntity found = commissionRepository.getByUser(user1);

        //then
        assertEquals(found.getUser(), user1);
    }

    @Test
    public void whenGetCommissionsByListId_thenReturnCommissionInCurrentListId() {
        //given
        Integer listId = 3;
        Integer subListId = 5;
        CommissionEntity commission1 = new CommissionEntity();
        commission1.setListId(listId);

        CommissionEntity commission2 = new CommissionEntity();
        commission2.setListId(subListId);

        entityManager.persist(commission1);
        entityManager.persist(commission2);
        entityManager.flush();

        //when
        List<CommissionEntity> found = commissionRepository.getCommissionEntitiesByListId(listId);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getListId(), listId);
    }
}
