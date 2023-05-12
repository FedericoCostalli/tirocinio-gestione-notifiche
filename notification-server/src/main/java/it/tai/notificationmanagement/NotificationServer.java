package it.tai.notificationmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import it.tai.notificationmanagement.server.config.ApplicationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class NotificationServer {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServer.class, args);
	}

}
