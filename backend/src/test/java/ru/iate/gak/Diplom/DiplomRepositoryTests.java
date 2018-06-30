package ru.iate.gak.Diplom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.DiplomEntity;
import ru.iate.gak.model.StudentEntity;
import ru.iate.gak.repository.DiplomRepository;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DiplomRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DiplomRepository diplomRepository;

    @Test
    public void whenGetByStudent_thenReturnListWhereStudentContains() {
        //given
        StudentEntity student1 = new StudentEntity();
        StudentEntity student2 = new StudentEntity();
        entityManager.persist(student1);
        entityManager.persist(student2);

        DiplomEntity diplom1 = new DiplomEntity();
        diplom1.setStudent(student1);
        DiplomEntity diplom2 = new DiplomEntity();
        diplom2.setStudent(student2);
        entityManager.persist(diplom1);
        entityManager.persist(diplom2);

        //when
        DiplomEntity found = diplomRepository.getByStudent(student1);

        //then
        assertEquals(found.getStudent(), student1);
    }
}
