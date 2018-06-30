package ru.iate.gak.Group;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.repository.GroupRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void whenGetByOrderTitleAsc_thenReturnListOfSortedAsc() {
        //given
        GroupEntity group1 = new GroupEntity("Main");
        GroupEntity group2 = new GroupEntity("Sub");
        GroupEntity group3 = new GroupEntity("Absolutely");
        entityManager.persist(group1);
        entityManager.persist(group2);
        entityManager.persist(group3);
        entityManager.flush();

        //when
        List<GroupEntity> found = groupRepository.getAllOrderByTitleAsc();

        //then
        assertEquals(found.size(), 3);
        assertEquals(found.get(0).getTitle(), group3.getTitle());
        assertEquals(found.get(1).getTitle(), group1.getTitle());
        assertEquals(found.get(2).getTitle(), group2.getTitle());
    }

    @Test
    public void whenGetByOrderTitleAscWithRussianSymbolsAndHyphen() {       //russian literals, numbers, -
        //given
        GroupEntity group1 = new GroupEntity("ИС-Б14");
        GroupEntity group2 = new GroupEntity("ACУ-М13");
        GroupEntity group3 = new GroupEntity("БАК-С3Б");
        GroupEntity group4 = new GroupEntity("ИВТ-Б14");
        entityManager.persist(group1);
        entityManager.persist(group2);
        entityManager.persist(group3);
        entityManager.persist(group4);
        entityManager.flush();

        //when
        List<GroupEntity> found = groupRepository.getAllOrderByTitleAsc();

        //then
        assertEquals(found.size(), 4);
        assertEquals(found.get(0).getTitle(), group2.getTitle());
        assertEquals(found.get(1).getTitle(), group3.getTitle());
        assertEquals(found.get(2).getTitle(), group4.getTitle());
        assertEquals(found.get(3).getTitle(), group1.getTitle());
    }
}
