package com.emergency.emergencyservice.dto;

import com.emergency.emergencyservice.entity.IncidentStatus;
import com.emergency.emergencyservice.entity.IncidentType;

import java.time.Instant;
import java.util.UUID;

public record IncidentResponse(
    UUID id,
    IncidentType type,
    IncidentStatus status,
    Integer severity,
    String description,
    UUID reporterId,
    Double latitude,
    Double longitude,
    String address,
    String city,
    Short priorityScore,
    Instant reportedAt
) {}
