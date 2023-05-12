package it.tai.notificationmanagement.server.domain;

import it.tai.notificationmanagement.domain.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private String datetime;
    private Contact customer;
}
