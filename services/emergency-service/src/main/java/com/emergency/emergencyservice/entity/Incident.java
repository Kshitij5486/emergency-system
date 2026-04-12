package com.emergency.emergencyservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "incidents", schema = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "incidents.incident_type")
    private IncidentType type;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "incidents.incident_status")
    private IncidentStatus status = IncidentStatus.REPORTED;

    @Column(nullable = false)
    private Integer severity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "reporter_id", nullable = false)
    private UUID reporterId;

    @Column(name = "assigned_responder_id")
    private UUID assignedResponderId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 500)
    private String address;

    @Column(nullable = false)
    private String city = "default";

    @Column(name = "priority_score")
    private Short priorityScore;

    @Column(name = "fake_probability")
    private Double fakeProbability;

    @CreationTimestamp
    @Column(name = "reported_at", updatable = false)
    private Instant reportedAt;

    @Column(name = "dispatched_at")
    private Instant dispatchedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public IncidentType getType() { return type; }
    public void setType(IncidentType type) { this.type = type; }
    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }
    public Integer getSeverity() { return severity; }
    public void setSeverity(Integer severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public UUID getReporterId() { return reporterId; }
    public void setReporterId(UUID reporterId) { this.reporterId = reporterId; }
    public UUID getAssignedResponderId() { return assignedResponderId; }
    public void setAssignedResponderId(UUID id) { this.assignedResponderId = id; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Short getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Short priorityScore) { this.priorityScore = priorityScore; }
    public Double getFakeProbability() { return fakeProbability; }
    public void setFakeProbability(Double fakeProbability) { this.fakeProbability = fakeProbability; }
    public Instant getReportedAt() { return reportedAt; }
    public Instant getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(Instant dispatchedAt) { this.dispatchedAt = dispatchedAt; }
    public Instant getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Instant resolvedAt) { this.resolvedAt = resolvedAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
