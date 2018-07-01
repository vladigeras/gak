package ru.iate.gak.Criteria;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.service.CriteriaService;
import ru.iate.gak.service.impl.CriteriaServiceImpl;

@RunWith(SpringRunner.class)
public class CriteriaServiceImplTests {

    @TestConfiguration
    static class CriteriaServiceImplTestsContextConfiguration {
        @Bean
        public CriteriaService criteriaService() {
            return new CriteriaServiceImpl();
        }
    }
}
