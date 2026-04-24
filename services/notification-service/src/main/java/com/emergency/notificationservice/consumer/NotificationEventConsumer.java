package com.emergency.notificationservice.consumer;

import com.emergency.notificationservice.event.EmergencyCreatedEvent;
import com.emergency.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);
    private final NotificationService notificationService;

    public NotificationEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
        topics = "emergency-events",
        groupId = "notification-service-group"
    )
    public void consumeEmergencyCreated(
            @Payload EmergencyCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Notification service received event: incidentId={}, type={}, city={}",
                event.getIncidentId(), event.getType(), event.getCity());

        try {
            notificationService.sendEmergencyAlert(event);
        } catch (Exception e) {
            log.error("Failed to send notification for incident {}: {}",
                    event.getIncidentId(), e.getMessage(), e);
        }
    }
}