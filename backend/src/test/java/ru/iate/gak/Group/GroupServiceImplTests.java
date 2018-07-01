package ru.iate.gak.Group;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.service.GroupService;
import ru.iate.gak.service.impl.GroupServiceImpl;

@RunWith(SpringRunner.class)
public class GroupServiceImplTests {

    @TestConfiguration
    static class GroupServiceImplTestsContextConfiguration {
        @Bean
        public GroupService groupService() {
            return new GroupServiceImpl();
        }
    }
}
