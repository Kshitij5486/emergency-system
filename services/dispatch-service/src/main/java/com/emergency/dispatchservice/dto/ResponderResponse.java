package com.emergency.dispatchservice.dto;

public record ResponderResponse(
    String id,
    String name,
    String type,
    String status,
    String phoneNumber,
    String vehicleId,
    Double latitude,
    Double longitude,
    String city,
    String currentIncidentId
) {}
