package it.tai.notificationmanagement.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import ch.qos.logback.classic.Logger;
import it.tai.notificationmanagement.StreamLambdaHandler;
import it.tai.notificationmanagement.domain.Contact;

import org.slf4j.LoggerFactory;

public class AppointmentServiceTest {

	private static final Logger logger
			= (Logger) LoggerFactory.getLogger(AppointmentServiceTest.class);

	public static void main(String[] args) throws Exception{
		StreamLambdaHandler handler = new StreamLambdaHandler();
		handler.handleRequest(null, null, null);

	}

}
