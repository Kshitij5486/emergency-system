package com.emergency.emergencyservice.service;

import com.emergency.emergencyservice.dto.CreateIncidentRequest;
import com.emergency.emergencyservice.dto.IncidentResponse;
import com.emergency.emergencyservice.entity.Incident;
import com.emergency.emergencyservice.entity.IncidentStatus;
import com.emergency.emergencyservice.entity.IncidentType;
import com.emergency.emergencyservice.repository.IncidentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    private static final Logger log = LoggerFactory.getLogger(IncidentService.class);
    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Transactional
    public IncidentResponse createIncident(CreateIncidentRequest request, UUID reporterId) {
        IncidentType type;
        try {
            type = IncidentType.valueOf(request.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid incident type: " + request.type());
        }

        Incident incident = new Incident();
        incident.setType(type);
        incident.setStatus(IncidentStatus.REPORTED);
        incident.setSeverity(request.severity());
        incident.setDescription(request.description());
        incident.setReporterId(reporterId);
        incident.setLatitude(request.latitude());
        incident.setLongitude(request.longitude());
        incident.setAddress(request.address());
        incident.setCity(request.city().toLowerCase());

        Incident saved = incidentRepository.save(incident);
        log.info("Incident created: {} type={} severity={} city={}",
                saved.getId(), saved.getType(), saved.getSeverity(), saved.getCity());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> getMyIncidents(UUID reporterId) {
        return incidentRepository.findByReporterId(reporterId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> getActiveIncidents(String city) {
        return incidentRepository.findByCityAndStatus(city.toLowerCase(), IncidentStatus.REPORTED)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private IncidentResponse toResponse(Incident i) {
        return new IncidentResponse(
                i.getId(), i.getType(), i.getStatus(), i.getSeverity(),
                i.getDescription(), i.getReporterId(), i.getLatitude(),
                i.getLongitude(), i.getAddress(), i.getCity(),
                i.getPriorityScore(), i.getReportedAt());
    }
}
