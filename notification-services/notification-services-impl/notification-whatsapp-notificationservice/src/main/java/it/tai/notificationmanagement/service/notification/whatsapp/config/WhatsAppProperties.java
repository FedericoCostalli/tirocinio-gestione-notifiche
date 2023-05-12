package it.tai.notificationmanagement.service.notification.whatsapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "application.whatsapp", ignoreUnknownFields = true)
@Data
public class WhatsAppProperties {

	private String apiUrl = "http://localhost:8002";
}
