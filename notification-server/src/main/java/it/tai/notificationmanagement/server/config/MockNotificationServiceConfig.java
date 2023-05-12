package it.tai.notificationmanagement.server.config;

import it.tai.notificationmanagement.service.MockNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Slf4j
public class MockNotificationServiceConfig {
    @Bean
    @ConditionalOnProperty(prefix = "application.notification", name = "service", havingValue = "mock", matchIfMissing = true)
    public MockNotificationService buildMockNotificationService(){
        log.info("Creating Mock Notification Service");
        return new MockNotificationService();
    }

}
