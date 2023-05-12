package it.tai.notificationmanagement.service.notification.whatsapp.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import it.tai.notificationmanagement.service.notification.whatsapp.WhatsAppNotificationService;
import it.tai.notificationmanagement.service.notification.whatsapp.support.WhatsAppAPI;

@Configuration
public class WhatsAppNotificationConfig {

	@Bean
	@ConditionalOnProperty(prefix = "application.notification", name = "service", havingValue = "whatsapp", matchIfMissing = false)
	public WhatsAppNotificationService buildWhatsAppNotificationService(WhatsAppAPI whatsAppClient) {
		return new WhatsAppNotificationService(whatsAppClient);	}
	@Bean
	public WhatsAppAPI buildWhatsAppAPI(WhatsAppProperties properties) {
		var mapper = new ObjectMapper()
				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		 return Feign.builder().encoder(new
				  JacksonEncoder(mapper)).decoder(new JacksonDecoder(mapper))
				 .target(WhatsAppAPI.class, properties.getApiUrl());	
	}
		
}
