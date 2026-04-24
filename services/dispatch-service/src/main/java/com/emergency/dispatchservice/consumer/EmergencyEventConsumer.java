package com.emergency.dispatchservice.consumer;

import com.emergency.dispatchservice.event.EmergencyCreatedEvent;
import com.emergency.dispatchservice.service.DispatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmergencyEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmergencyEventConsumer.class);

    private final DispatchService dispatchService;

    public EmergencyEventConsumer(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @KafkaListener(
        topics = "emergency-events",
        groupId = "dispatch-service-group",
        concurrency = "3"
    )
    public void consumeEmergencyCreated(
            @Payload EmergencyCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Received emergency event: incidentId={}, type={}, city={}, partition={}, offset={}",
                event.getIncidentId(), event.getType(), event.getCity(), partition, offset);

        try {
            dispatchService.handleEmergencyCreated(event);
        } catch (Exception e) {
            log.error("Failed to process emergency event {}: {}", event.getIncidentId(), e.getMessage(), e);
            throw e;
        }
    }
}