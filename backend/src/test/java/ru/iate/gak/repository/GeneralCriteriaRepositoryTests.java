package ru.iate.gak.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.GeneralCriteriaEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GeneralCriteriaRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GeneralCriteriaRepository generalCriteriaRepository;

    @Test
    public void whenGetByListId_thenReturnArrayWithListIdContains() {
        //given
        Integer listId = 1;
        Integer subListId = 2;

        GeneralCriteriaEntity criteria1 = new GeneralCriteriaEntity();
        criteria1.setListId(listId);
        GeneralCriteriaEntity criteria2 = new GeneralCriteriaEntity();
        criteria2.setListId(subListId);

        entityManager.persist(criteria1);
        entityManager.persist(criteria2);
        entityManager.flush();

        //when
        List<GeneralCriteriaEntity> found = generalCriteriaRepository.getAllByListId(listId);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getListId(), listId);
    }
}
