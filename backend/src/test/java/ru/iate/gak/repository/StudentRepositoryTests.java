package ru.iate.gak.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.model.StudentEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void whenGetByGroupAndNullDeletedTime_thenReturnListWhereConditionTrue() {
        //given
        GroupEntity group1 = new GroupEntity("Group1");
        GroupEntity group2 = new GroupEntity("Group2");
        entityManager.persist(group1);
        entityManager.persist(group2);

        StudentEntity student1 = new StudentEntity();
        student1.setGroup(group1);

        StudentEntity student2 = new StudentEntity();
        student2.setGroup(group2);

        StudentEntity student3 = new StudentEntity();
        student3.setGroup(group1);
        student3.setDeletedTime(LocalDateTime.now(ZoneOffset.UTC));

        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.persist(student3);
        entityManager.flush();

        //when
        List<StudentEntity> found = studentRepository.getAllByGroupAndDeletedTimeIsNull(group1);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getGroup(), group1);
        assertNull(found.get(0).getDeletedTime());
    }

}
