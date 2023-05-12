package it.tai.notificationmanagement.server.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import it.tai.notificationmanagement.domain.*;
import it.tai.notificationmanagement.server.config.ApplicationProperties;
import it.tai.notificationmanagement.service.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.tai.notificationmanagement.server.domain.Appointment;
import lombok.AllArgsConstructor;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@AllArgsConstructor
@Service
public class AppointmentService {

	private final ContactService contactService;

	private final MessageHistoryRepository messageHistoryRepository;
	private final SessionAPI sessionAPI;
	private final BookingAPI bookingAPI;
	private final ApplicationProperties.AdminProperties adminProperties;

	private final List<NotificationService> notificationServices;


	public ArrayList<Appointment> getNewAppointments() {

		LocalDate from_date = LocalDate.now();
		String from = from_date.toString();
		String to = from_date.plus(1, ChronoUnit.DAYS).toString();
		sessionAPI.createSession(adminProperties.getUsername(), adminProperties.getPassword());
		var fresha_data = bookingAPI.findAll(from,to);

		ArrayList<Appointment> appointments_to_notify = new ArrayList<>();

		for (GenericEntity entity : fresha_data.getData()) {
			Contact current_customer = new Contact();
			Appointment current_appointment = new Appointment();
			current_appointment.setDatetime((String) entity.getAttributes().get("start"));
			String current_customer_id = entity.getRelationships().get("customer").getData().get(0).getId();
			for (GenericEntity entity1 : fresha_data.getIncluded()) {
				if (entity1.getId().equalsIgnoreCase(current_customer_id)) {
					current_customer.setName((String) entity1.getAttributes().get("name"));
					current_customer.setPhoneNumber((String) entity1.getAttributes().get("contact-number"));
				}
			}
			current_appointment.setCustomer(current_customer);
			appointments_to_notify.add(current_appointment);
		}
		log.debug("Appointments to notify: {}", appointments_to_notify);
		return appointments_to_notify;

	}


	public void sendAppointmentsToAdmin(String target, ArrayList<Appointment> appointments){
		for (Appointment appointment : appointments) {


			LocalDateTime dateTime = LocalDateTime.parse(appointment.getDatetime(),
					DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'di' MMMM 'alle' HH:mm")
					.withLocale(Locale.ITALIAN);
			String formattedDateTime = dateTime.format(formatter);


			String body = "Invierò i seguenti messaggi: \n " + appointment.getCustomer().getName() + " ->  "
					+ formattedDateTime +"\n";
			if(appointment.getCustomer().getPhoneNumber()!=null) {
				Notification notification = Notification.builder()
						.body(body).target(adminProperties.getPhonenumber()).build();


				for (NotificationService notificationService : notificationServices) {
					if (notificationService.supports(target))
						notificationService.sendNotification(notification);
					else
						log.debug("Didn't send notification {} because {} is not supported", notification, target);
				}
			}else{
				String body_err = "Impossibile mandare il messaggio a" + appointment.getCustomer().getName() + " perché" +
						"non è presente il numero";
				Notification notification = Notification.builder()
						.body(body_err).target(adminProperties.getPhonenumber()).build();


				for (NotificationService notificationService : notificationServices) {
					if (notificationService.supports(target))
						notificationService.sendNotification(notification);
					else
						log.debug("Didn't send notification {} because {} is not supported", notification, target);
				}
				log.info("Couldn't send appointment {} because there's no phone number", appointment);
			}
		}


		String body = "Per eseguire un nuovo test cliccare su:\n"+ adminProperties.getApiUrl()+"api/appointment/appointments/admin\n\n" +
				"Per inviare i messaggi cliccare su\n"+adminProperties.getApiUrl()+"api/appointment/appointments/messagesender";


		Notification notification = Notification.builder()
				.body(body).target(adminProperties.getPhonenumber()).build();


		for (NotificationService notificationService : notificationServices) {
			if (notificationService.supports(target))
				notificationService.sendNotification(notification);
			else
				log.debug("Didn't send notification {} because {} is not supported", notification, target);
		}


	}


	public void sendAppointments(String target, ArrayList<Appointment> appointments) {
		for (Appointment appointment : appointments) {

			LocalDateTime dateTime = LocalDateTime.parse(appointment.getDatetime(),
					DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'di' MMMM 'alle' HH:mm")
					.withLocale(Locale.ITALIAN);
			String formattedDateTime = dateTime.format(formatter);

			String body = "Gentile " + appointment.getCustomer().getName() + " le ricordiamo che ha un appuntamento " +
					"il " + formattedDateTime;
			if(appointment.getCustomer().getPhoneNumber()!=null) {
				Notification notification = Notification.builder()
						.body(body).target(appointment.getCustomer().getPhoneNumber()).build();

				DateTimeFormatter formatter_check = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String date_check = dateTime.format(formatter_check);
				AppointmentCheck appointmentCheck = AppointmentCheck.builder().
						date(date_check).phone_number(appointment.getCustomer().getPhoneNumber()).build();

				for (NotificationService notificationService : notificationServices) {
					if (notificationService.supports(target) && !messageHistoryRepository.hasAlreadyBeenSent(appointmentCheck)) {
						if(!contactService.existsContactByPhoneNumber(notification.getTarget()))
							contactService.putContact(appointment.getCustomer());
						notificationService.sendNotification(notification);
						messageHistoryRepository.markAsSent(notification);
					}
					else
						log.debug("Didn't send notification {} because {} is not supported", notification, target);
				}
			}else{
				log.info("Couldn't send appointment {} because there's no phone number", appointment);
			}
		}
	}


}
