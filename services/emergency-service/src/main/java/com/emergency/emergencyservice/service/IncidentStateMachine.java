package com.emergency.emergencyservice.service;

import com.emergency.emergencyservice.entity.IncidentStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class IncidentStateMachine {

    private static final Map<IncidentStatus, Set<IncidentStatus>> ALLOWED_TRANSITIONS =
            new EnumMap<>(IncidentStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(IncidentStatus.REPORTED,
                Set.of(IncidentStatus.QUEUED, IncidentStatus.CANCELLED, IncidentStatus.FAKE));
        ALLOWED_TRANSITIONS.put(IncidentStatus.QUEUED,
                Set.of(IncidentStatus.DISPATCHED, IncidentStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(IncidentStatus.DISPATCHED,
                Set.of(IncidentStatus.IN_PROGRESS, IncidentStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(IncidentStatus.IN_PROGRESS,
                Set.of(IncidentStatus.RESOLVED, IncidentStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(IncidentStatus.RESOLVED, Set.of());
        ALLOWED_TRANSITIONS.put(IncidentStatus.CANCELLED, Set.of());
        ALLOWED_TRANSITIONS.put(IncidentStatus.FAKE, Set.of());
    }

    public boolean isTransitionAllowed(IncidentStatus from, IncidentStatus to) {
        Set<IncidentStatus> allowed = ALLOWED_TRANSITIONS.get(from);
        return allowed != null && allowed.contains(to);
    }

    public void validateTransition(IncidentStatus from, IncidentStatus to) {
        if (!isTransitionAllowed(from, to)) {
            throw new IllegalStateException(
                "Invalid status transition: " + from + " -> " + to +
                ". Allowed from " + from + ": " + ALLOWED_TRANSITIONS.get(from)
            );
        }
    }

    public Set<IncidentStatus> getAllowedTransitions(IncidentStatus from) {
        return ALLOWED_TRANSITIONS.getOrDefault(from, Set.of());
    }
}
