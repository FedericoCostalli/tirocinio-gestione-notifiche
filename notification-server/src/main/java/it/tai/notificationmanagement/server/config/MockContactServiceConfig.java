package it.tai.notificationmanagement.server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.services.people.v1.PeopleService;

import it.tai.notificationmanagement.service.GoogleContactService;
import it.tai.notificationmanagement.service.MockContactService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AllArgsConstructor
@Slf4j
public class MockContactServiceConfig {	
	
	@Bean
	@ConditionalOnProperty(prefix = "application.contact", name = "service", havingValue = "mock", matchIfMissing = true)
	public MockContactService buildMockContactService() {
		log.info("Creating Mock Contact Service");
		return new MockContactService();
	}
}
