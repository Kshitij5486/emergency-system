package com.emergency.emergencyservice.dto;

import jakarta.validation.constraints.*;

public record CreateIncidentRequest(

    @NotNull(message = "Incident type is required")
    String type,

    @NotNull(message = "Severity is required")
    @Min(value = 1, message = "Severity must be at least 1")
    @Max(value = 5, message = "Severity must be at most 5")
    Integer severity,

    String description,

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Invalid latitude")
    @DecimalMax(value = "90.0", message = "Invalid latitude")
    Double latitude,

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Invalid longitude")
    @DecimalMax(value = "180.0", message = "Invalid longitude")
    Double longitude,

    String address,

    @NotBlank(message = "City is required")
    String city
) {}
