package ru.iate.gak.Student;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.service.StudentService;
import ru.iate.gak.service.impl.StudentServiceImpl;

@RunWith(SpringRunner.class)
public class StudentServiceImplTests {

    @TestConfiguration
    static class StudentServiceImplTestsContextConfiguration {
        @Bean
        public StudentService studentService() {
            return new StudentServiceImpl();
        }
    }
}
