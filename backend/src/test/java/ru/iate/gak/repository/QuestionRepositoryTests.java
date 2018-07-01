package ru.iate.gak.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.QuestionEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void whenGetAllByDiplom_thenReturnListWithCurrentDiplomContains() {
        //given
        DiplomEntity diplom1 = new DiplomEntity();
        DiplomEntity diplom2 = new DiplomEntity();
        entityManager.persist(diplom1);
        entityManager.persist(diplom2);

        QuestionEntity question1 = new QuestionEntity();
        question1.setDiplom(diplom1);
        QuestionEntity question2 = new QuestionEntity();
        question2.setDiplom(diplom2);
        entityManager.persist(question1);
        entityManager.persist(question1);
        entityManager.flush();

        //when
        List<QuestionEntity> found = questionRepository.getAllByDiplom(diplom1);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getDiplom(), diplom1);
    }
}
