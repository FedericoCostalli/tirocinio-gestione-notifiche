package it.tai.notificationmanagement.service.notification.whatsapp.support.domain;

import lombok.Data;

@Data
public class SendStatus {

	private boolean success;
	
	private SendError error;
}
