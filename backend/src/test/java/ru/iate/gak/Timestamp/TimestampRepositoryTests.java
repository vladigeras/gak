package ru.iate.gak.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.TimestampEntity;
import ru.iate.gak.repository.TimestampRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TimestampRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimestampRepository timestampRepository;

    @Test
    public void whenGetAllByDiplom_thenReturnListWithCurrentDiplomContains() {
        //given
        DiplomEntity diplom1 = new DiplomEntity();
        DiplomEntity diplom2 = new DiplomEntity();
        entityManager.persist(diplom1);
        entityManager.persist(diplom2);

        TimestampEntity timestamp1 = new TimestampEntity();
        timestamp1.setDiplom(diplom1);
        TimestampEntity timestamp2 = new TimestampEntity();
        timestamp2.setDiplom(diplom2);
        entityManager.persist(timestamp1);
        entityManager.persist(timestamp2);
        entityManager.flush();

        //when
        List<TimestampEntity> found = timestampRepository.getAllByDiplom(diplom1);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getDiplom(), diplom1);
    }
}
