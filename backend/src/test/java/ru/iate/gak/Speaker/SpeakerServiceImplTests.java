package ru.iate.gak.Speaker;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iate.gak.service.SpeakerService;
import ru.iate.gak.service.impl.SpeakerServiceImpl;

@RunWith(SpringRunner.class)
public class SpeakerServiceImplTests {

    @TestConfiguration
    static class SpeakerServiceImplTestsContextConfiguration {
        @Bean
        public SpeakerService speakerService() {
            return new SpeakerServiceImpl();
        }
    }
}
