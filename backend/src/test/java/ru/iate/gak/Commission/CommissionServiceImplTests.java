package ru.iate.gak.Commission;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.service.CommissionService;
import ru.iate.gak.service.impl.CommissionServiceImpl;

@RunWith(SpringRunner.class)
public class CommissionServiceImplTests {

    @TestConfiguration
    static class CommissionServiceImplTestsContextConfiguration {
        @Bean
        public CommissionService commissionService() {
            return new CommissionServiceImpl();
        }
    }
}
