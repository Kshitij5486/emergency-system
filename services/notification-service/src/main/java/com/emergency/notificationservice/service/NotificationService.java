package com.emergency.notificationservice.service;

import com.emergency.notificationservice.event.EmergencyCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendEmergencyAlert(EmergencyCreatedEvent event) {
        String message = buildAlertMessage(event);
        log.info("=== EMERGENCY ALERT ===");
        log.info("Incident ID : {}", event.getIncidentId());
        log.info("Type        : {}", event.getType());
        log.info("Severity    : {}/5", event.getSeverity());
        log.info("City        : {}", event.getCity());
        log.info("Location    : {}, {}", event.getLatitude(), event.getLongitude());
        log.info("Message     : {}", message);
        log.info("======================");

        sendSmsSimulated(message, event.getCity());
    }

    private void sendSmsSimulated(String message, String city) {
        log.info("[SMS SIMULATED] To: Emergency responders in {} | Message: {}", city, message);
        log.info("[PUSH SIMULATED] Alert sent to all responder mobile apps in {}", city);
    }

    private String buildAlertMessage(EmergencyCreatedEvent event) {
        return String.format(
            "EMERGENCY ALERT: %s incident (Severity %d/5) reported in %s at coordinates %.4f, %.4f. Respond immediately.",
            event.getType(), event.getSeverity(), event.getCity(),
            event.getLatitude(), event.getLongitude()
        );
    }
}