package it.tai.notificationmanagement.service;

import it.tai.notificationmanagement.domain.Notification;

public interface NotificationService {

	void sendNotification(Notification notification);
	
	boolean supports(String targetType);
}
