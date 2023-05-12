package it.tai.notificationmanagement.service;

import it.tai.notificationmanagement.domain.AppointmentCheck;
import it.tai.notificationmanagement.domain.Notification;

import java.util.ArrayList;

public interface MessageHistoryRepository {

    boolean hasAlreadyBeenSent(AppointmentCheck appointmentCheck);

    void markAsSent(Notification notification);

}
