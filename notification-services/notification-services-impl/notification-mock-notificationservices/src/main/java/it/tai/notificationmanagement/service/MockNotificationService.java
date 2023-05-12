package it.tai.notificationmanagement.service;

import it.tai.notificationmanagement.domain.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockNotificationService implements NotificationService{

    @Override
    public void sendNotification(Notification notification) {
        log.info("Sending notification {}",notification);
    }

    @Override
    public boolean supports(String targetType) {
        return true;
    }
}