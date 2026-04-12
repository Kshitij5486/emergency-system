package com.emergency.userservice.dto;

import java.util.Set;
import java.util.UUID;

public record RegisterResponse(
    UUID id,
    String fullName,
    String email,
    String phoneNumber,
    Set<String> roles
) {}