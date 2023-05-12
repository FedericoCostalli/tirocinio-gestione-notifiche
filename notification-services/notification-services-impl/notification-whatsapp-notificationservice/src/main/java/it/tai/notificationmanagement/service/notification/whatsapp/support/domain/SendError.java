package it.tai.notificationmanagement.service.notification.whatsapp.support.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class SendError implements Serializable{

	private String name;
	private String message;
	
}
