package com.emergency.userservice.dto;

import java.util.Set;
import java.util.UUID;

public record LoginResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    UUID userId,
    String email,
    String fullName,
    Set<String> roles
) {}