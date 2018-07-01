package ru.iate.gak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.TestsConfig;
import ru.iate.gak.domain.Group;
import ru.iate.gak.model.GroupEntity;
import ru.iate.gak.repository.GroupRepository;
import ru.iate.gak.service.impl.GroupServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        TestsConfig.class,
        GroupServiceImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GroupServiceImplTests {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void whenGetGroups_returnListOfGroups() {
        //given
        groupRepository.save(new GroupEntity("ИС-Б14"));

        //when
        List<Group> found = groupService.getGroups();

        //then
        assertEquals(found.size(), 1);
    }
}
