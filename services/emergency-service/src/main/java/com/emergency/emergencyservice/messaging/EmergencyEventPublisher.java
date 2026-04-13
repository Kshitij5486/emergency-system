package com.emergency.emergencyservice.messaging;

import com.emergency.emergencyservice.event.EmergencyCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class EmergencyEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EmergencyEventPublisher.class);
    private static final String TOPIC = "emergency-events";

    private final KafkaTemplate<String, EmergencyCreatedEvent> kafkaTemplate;

    public EmergencyEventPublisher(KafkaTemplate<String, EmergencyCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEmergencyCreated(EmergencyCreatedEvent event) {
        String key = event.getIncidentId().toString();

        CompletableFuture<SendResult<String, EmergencyCreatedEvent>> future =
                kafkaTemplate.send(TOPIC, key, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish emergency event for incident {}: {}",
                        event.getIncidentId(), ex.getMessage());
            } else {
                log.info("Published emergency event: incidentId={}, partition={}, offset={}",
                        event.getIncidentId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}