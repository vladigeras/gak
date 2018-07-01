package ru.iate.gak.Question;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.service.QuestionService;
import ru.iate.gak.service.impl.QuestionServiceImpl;

@RunWith(SpringRunner.class)
public class QuestionServiceImplTests {

    @TestConfiguration
    static class QuestionServiceImplTestsContextConfiguration {
        @Bean
        public QuestionService questionService() {
            return new QuestionServiceImpl();
        }
    }
}
