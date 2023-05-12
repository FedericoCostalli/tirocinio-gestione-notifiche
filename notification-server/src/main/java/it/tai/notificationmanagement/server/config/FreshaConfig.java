package it.tai.notificationmanagement.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import feign.Feign;
import feign.Feign.Builder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import it.tai.notificationmanagement.server.config.ApplicationProperties.FreshaProperties;
import it.tai.notificationmanagement.server.interceptor.SessionIdRequestInterceptor;
import it.tai.notificationmanagement.service.BookingAPI;
import it.tai.notificationmanagement.service.CustomerAPI;
import it.tai.notificationmanagement.service.MessagesAPI;
import it.tai.notificationmanagement.service.SessionAPI;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class FreshaConfig {

	private final ApplicationProperties applicationProperties;
	
	private final Builder builder = Feign.builder().client(new OkHttpClient(
			new okhttp3.OkHttpClient.Builder().addInterceptor(new SessionIdRequestInterceptor()).build()));
	private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
			.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.configure(SerializationFeature.INDENT_OUTPUT, true)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final JacksonEncoder jacksonEncoder = new JacksonEncoder(mapper);
	private final JacksonDecoder jacksonDecoder = new JacksonDecoder(mapper);
	
	private FreshaProperties getFreshaProperties() {
		return applicationProperties.getFresha();
	}
	

	
	@Bean
	public SessionAPI buildSessionAPI() {
		
		
		return  builder.target(SessionAPI.class, getFreshaProperties().getApiUrl());
	}
	@Bean
	public MessagesAPI buildMessagesAPI() {
	return builder.encoder(jacksonEncoder).decoder(jacksonDecoder)
			.target(MessagesAPI.class, getFreshaProperties().getNotificationUrl());
	}
	
	@Bean
	public BookingAPI buildBookingAPI() {
		return builder.encoder(jacksonEncoder).decoder(jacksonDecoder)
				.target(BookingAPI.class, getFreshaProperties().getApiUrl());
		
	}
	
	@Bean
	public CustomerAPI buildCustomerAPI() {
		return builder.encoder(jacksonEncoder).decoder(jacksonDecoder)
				.target(CustomerAPI.class,getFreshaProperties().getCustomerUrl());
		
	}
	

	
	
	
}
