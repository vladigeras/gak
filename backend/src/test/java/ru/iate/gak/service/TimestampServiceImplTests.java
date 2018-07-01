package ru.iate.gak.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.service.impl.TimestampServiceImpl;

@RunWith(SpringRunner.class)
public class TimestampServiceImplTests {

    @TestConfiguration
    static class TimestampServiceImplTestsContextConfiguration {
        @Bean
        public TimestampService timestampService() {
            return new TimestampServiceImpl();
        }
    }
}
