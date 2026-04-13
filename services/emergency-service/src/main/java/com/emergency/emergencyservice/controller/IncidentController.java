package com.emergency.emergencyservice.controller;

import com.emergency.emergencyservice.dto.CreateIncidentRequest;
import com.emergency.emergencyservice.dto.IncidentResponse;
import com.emergency.emergencyservice.dto.UpdateStatusRequest;
import com.emergency.emergencyservice.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(
            @Valid @RequestBody CreateIncidentRequest request,
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(incidentService.createIncident(request, UUID.fromString(userId)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest request,
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(incidentService.updateStatus(id, request, UUID.fromString(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<IncidentResponse>> myIncidents(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(incidentService.getMyIncidents(UUID.fromString(userId)));
    }

    @GetMapping("/active")
    public ResponseEntity<List<IncidentResponse>> activeIncidents(
            @RequestParam String city) {
        return ResponseEntity.ok(incidentService.getActiveIncidents(city));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "service", "emergency-service",
            "status", "UP",
            "version", "1.0.0"
        ));
    }
}
