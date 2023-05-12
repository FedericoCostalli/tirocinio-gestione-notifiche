package it.tai.notificationmanagement.server.service.web;

import it.tai.notificationmanagement.server.domain.Appointment;
import it.tai.notificationmanagement.server.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
public class AppointmentResource {

    private final AppointmentService appointmentservice;

    @GetMapping("/appointments/admin")
    public void customerCheck() throws Exception {
        appointmentservice.sendAppointmentsToAdmin("whatsapp",appointmentservice.getNewAppointments());
    }
    @GetMapping("/appointments/messagesender")
    public void sendMessages(){
        appointmentservice.sendAppointments("whatsapp",appointmentservice.getNewAppointments());
    }

}
