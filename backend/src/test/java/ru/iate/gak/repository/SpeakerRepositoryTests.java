package ru.iate.gak.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.model.SpeakerEntity;
import ru.iate.gak.model.StudentEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpeakerRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Test
    public void whenGetByGroupAndDeleteTimeNull_thenReturnListWhereConditionsTrue() {
        //given
        GroupEntity group1 = new GroupEntity("Group1");
        GroupEntity group2 = new GroupEntity("Group2");
        entityManager.persist(group1);
        entityManager.persist(group2);

        StudentEntity student1 = new StudentEntity();
        student1.setGroup(group1);
        entityManager.persist(student1);

        StudentEntity student2 = new StudentEntity();
        student2.setGroup(group2);
        entityManager.persist(student2);

        StudentEntity student3 = new StudentEntity();
        student3.setGroup(group1);
        student3.setDeletedTime(LocalDateTime.now(ZoneOffset.UTC));
        entityManager.persist(student3);

        SpeakerEntity speaker1 = new SpeakerEntity();
        speaker1.setStudent(student1);

        SpeakerEntity speaker2 = new SpeakerEntity();
        speaker2.setStudent(student2);

        SpeakerEntity speaker3 = new SpeakerEntity();
        speaker3.setStudent(student3);

        entityManager.persist(speaker1);
        entityManager.persist(speaker2);
        entityManager.persist(speaker3);
        entityManager.flush();

        //when
        List<SpeakerEntity> found = speakerRepository.getSpeakersListOfCurrentGroup(group1);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getStudent().getGroup(), group1);

    }

    @Test
    public void whenDeleteByStudent_thenReturnListWithoutDeleted() {
        //given
        StudentEntity student1 = new StudentEntity();
        StudentEntity student2 = new StudentEntity();
        entityManager.persist(student1);
        entityManager.persist(student2);

        SpeakerEntity speaker1 = new SpeakerEntity();
        speaker1.setStudent(student1);
        SpeakerEntity speaker2 = new SpeakerEntity();
        speaker2.setStudent(student2);
        entityManager.persist(speaker1);
        entityManager.persist(speaker2);
        entityManager.flush();

        //when
        speakerRepository.deleteByStudent(student1);
        List<SpeakerEntity> found = speakerRepository.findAll();

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getStudent(), student2);
    }

    @Test
    public void whenGetByGroupAndDay_thenReturnListWhereConditionsTrue() {
        //given
        GroupEntity group = new GroupEntity("Group");
        entityManager.persist(group);

        StudentEntity student = new StudentEntity();
        student.setGroup(group);
        entityManager.persist(student);

        LocalDateTime day = LocalDateTime.now(ZoneOffset.UTC);

        SpeakerEntity speaker1 = new SpeakerEntity();
        speaker1.setStudent(student);
        speaker1.setDate(day);

        SpeakerEntity speaker2 = new SpeakerEntity();
        speaker2.setStudent(student);
        speaker2.setDate(day.plusDays(1));

        entityManager.persist(speaker1);
        entityManager.persist(speaker2);
        entityManager.flush();

        //when
        List<SpeakerEntity> found = speakerRepository.getSpeakersListOfCurrentGroupOfDay(group, day);

        //then
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getStudent().getGroup(), group);
        assertEquals(found.get(0).getDate(), day);
    }

    @Test
    public void whenGetByGroupAndBetweenDate_thenReturnListWhereConditionsTrue() {
        //given
        GroupEntity group = new GroupEntity("Group");
        entityManager.persist(group);

        StudentEntity student = new StudentEntity();
        student.setGroup(group);
        entityManager.persist(student);

        LocalDateTime prevDate = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime nextDate = prevDate.plusDays(1);

        SpeakerEntity speaker1 = new SpeakerEntity();
        speaker1.setStudent(student);
        speaker1.setDate(prevDate.plusHours(2));

        SpeakerEntity speaker2 = new SpeakerEntity();
        speaker2.setStudent(student);
        speaker2.setDate(prevDate.plusHours(12));

        SpeakerEntity speaker3 = new SpeakerEntity();
        speaker3.setStudent(student);
        speaker3.setDate(prevDate.plusHours(24).plusMinutes(2));

        entityManager.persist(speaker1);
        entityManager.persist(speaker2);
        entityManager.persist(speaker3);
        entityManager.flush();

        //when
        List<SpeakerEntity> found = speakerRepository.getSpeakersListOfCurrentGroupBetweenDate(group, prevDate, nextDate);

        //then
        assertEquals(found.size(), 2);
        assertEquals(found.get(0).getStudent().getGroup(), group);
        assertEquals(found.get(1).getStudent().getGroup(), group);
        assertTrue(found.get(0).getDate().isAfter(prevDate));
        assertTrue(found.get(0).getDate().isBefore(nextDate));
        assertTrue(found.get(1).getDate().isAfter(prevDate));
        assertTrue(found.get(1).getDate().isBefore(nextDate));
    }
}
