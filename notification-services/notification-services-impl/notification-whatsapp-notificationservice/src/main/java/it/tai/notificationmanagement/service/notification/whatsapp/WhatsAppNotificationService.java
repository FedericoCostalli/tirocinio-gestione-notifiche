package it.tai.notificationmanagement.service.notification.whatsapp;

import java.util.Optional;

import it.tai.notificationmanagement.domain.Notification;
import it.tai.notificationmanagement.service.NotificationService;
import it.tai.notificationmanagement.service.notification.whatsapp.support.WhatsAppAPI;
import it.tai.notificationmanagement.service.notification.whatsapp.support.domain.TextMessage;
import it.tai.notificationmanagement.service.notification.whatsapp.support.domain.TextMessageArgument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class WhatsAppNotificationService implements NotificationService {

	private WhatsAppAPI whatsAppClient;

	@Override
	public void sendNotification(Notification notification) {
		notification.setTarget(notification.getTarget().replaceAll("\\s", "").substring(1));
		log.debug("Sending {}", notification);
		var result = whatsAppClient.sendMTextMessage(TextMessageArgument.builder().args(
				TextMessage.builder().to(notification.getTarget() + "@c.us").content(notification.getBody()).build())
				.build());
		log.debug("Received {}",result);
		if(!result.isSuccess()){
			log.info("Something went wrong with sending the message");
		}

	}

	@Override
	public boolean supports(String targetType) {
		return Optional.ofNullable(targetType).map(value -> value.equalsIgnoreCase("whatsapp")).orElse(false);
	}

}
