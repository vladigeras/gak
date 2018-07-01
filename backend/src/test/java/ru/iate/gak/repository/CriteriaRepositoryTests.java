package ru.iate.gak.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.CommissionEntity;
import ru.iate.gak.model.CriteriaEntity;
import ru.iate.gak.model.DiplomEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CriteriaRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Test
    public void whenDeleteByCommission_thenReturnQuestionsWithoutDeleted() {
        //given
        CommissionEntity commission1 = new CommissionEntity();
        CommissionEntity commission2 = new CommissionEntity();
        entityManager.persist(commission1);
        entityManager.persist(commission2);

        CriteriaEntity criteria1 = new CriteriaEntity();
        criteria1.setCommission(commission1);
        CriteriaEntity criteria2 = new CriteriaEntity();
        criteria2.setCommission(commission2);
        entityManager.persist(criteria1);
        entityManager.persist(criteria2);

        List<CriteriaEntity> beforeDelete = criteriaRepository.findAll();
        int beforeDeleteCount = (int) beforeDelete.stream().filter(criteriaEntity -> criteriaEntity.getCommission().equals(commission1)).count();

        //when
        criteriaRepository.deleteByCommission(commission1);

        //then
        List<CriteriaEntity> afterDelete = criteriaRepository.findAll();
        int afterDeleteCount = (int) afterDelete.stream().filter(criteriaEntity -> criteriaEntity.getCommission().equals(commission1)).count();

        assertEquals(beforeDeleteCount, 1);
        assertEquals(afterDeleteCount, 0);
    }

    @Test
    public void whenGetCriteriaByDiplomId_thenReturnCriteriaListWithCurrentDiplomId() {
        //given
        DiplomEntity diplom1 = new DiplomEntity();
        DiplomEntity diplom2 = new DiplomEntity();
        entityManager.persist(diplom1);
        entityManager.persist(diplom2);

        CriteriaEntity criteria1 = new CriteriaEntity();
        CriteriaEntity criteria2 = new CriteriaEntity();
        criteria1.setDiplom(diplom1);
        criteria2.setDiplom(diplom2);
        entityManager.persist(criteria1);
        entityManager.persist(criteria2);

        //when
        List<CriteriaEntity> found = criteriaRepository.getCriteriaEntitiesByDiplomId(diplom1.getId());

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getDiplom().getId(), diplom1.getId());
    }

}
