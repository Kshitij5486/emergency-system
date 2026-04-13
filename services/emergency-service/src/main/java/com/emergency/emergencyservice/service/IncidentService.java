package com.emergency.emergencyservice.service;

import com.emergency.emergencyservice.dto.CreateIncidentRequest;
import com.emergency.emergencyservice.dto.IncidentResponse;
import com.emergency.emergencyservice.dto.UpdateStatusRequest;
import com.emergency.emergencyservice.entity.Incident;
import com.emergency.emergencyservice.entity.IncidentStatus;
import com.emergency.emergencyservice.entity.IncidentStatusHistory;
import com.emergency.emergencyservice.entity.IncidentType;
import com.emergency.emergencyservice.repository.IncidentRepository;
import com.emergency.emergencyservice.repository.IncidentStatusHistoryRepository;
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
    private final IncidentStatusHistoryRepository historyRepository;
    private final IncidentStateMachine stateMachine;

    public IncidentService(IncidentRepository incidentRepository,
                           IncidentStatusHistoryRepository historyRepository,
                           IncidentStateMachine stateMachine) {
        this.incidentRepository = incidentRepository;
        this.historyRepository = historyRepository;
        this.stateMachine = stateMachine;
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

        IncidentStatusHistory history = new IncidentStatusHistory();
        history.setIncidentId(saved.getId());
        history.setFromStatus(null);
        history.setToStatus(IncidentStatus.REPORTED);
        history.setChangedBy(reporterId);
        history.setNote("Incident reported");
        historyRepository.save(history);

        return toResponse(saved);
    }

    @Transactional
    public IncidentResponse updateStatus(UUID incidentId, UpdateStatusRequest request, UUID changedBy) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found: " + incidentId));

        IncidentStatus newStatus;
        try {
            newStatus = IncidentStatus.valueOf(request.status().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + request.status());
        }

        IncidentStatus oldStatus = incident.getStatus();
        stateMachine.validateTransition(oldStatus, newStatus);

        incident.setStatus(newStatus);

        if (newStatus == IncidentStatus.DISPATCHED) {
            incident.setDispatchedAt(java.time.Instant.now());
        } else if (newStatus == IncidentStatus.RESOLVED) {
            incident.setResolvedAt(java.time.Instant.now());
        }

        Incident saved = incidentRepository.save(incident);

        IncidentStatusHistory history = new IncidentStatusHistory();
        history.setIncidentId(saved.getId());
        history.setFromStatus(oldStatus);
        history.setToStatus(newStatus);
        history.setChangedBy(changedBy);
        history.setNote(request.note());
        historyRepository.save(history);

        log.info("Incident {} status: {} -> {}", incidentId, oldStatus, newStatus);
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

    @Transactional(readOnly = true)
    public IncidentResponse getById(UUID incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found: " + incidentId));
        return toResponse(incident);
    }

    private IncidentResponse toResponse(Incident i) {
        return new IncidentResponse(
                i.getId(), i.getType(), i.getStatus(), i.getSeverity(),
                i.getDescription(), i.getReporterId(), i.getLatitude(),
                i.getLongitude(), i.getAddress(), i.getCity(),
                i.getPriorityScore(), i.getReportedAt());
    }
}
