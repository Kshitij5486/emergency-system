package com.emergency.dispatchservice.event;

import java.time.Instant;
import java.util.UUID;

public class EmergencyCreatedEvent {

    private UUID incidentId;
    private String type;
    private int severity;
    private double latitude;
    private double longitude;
    private String city;
    private UUID reporterId;
    private int priorityScore;
    private Instant reportedAt;

    public EmergencyCreatedEvent() {}

    public UUID getIncidentId() { return incidentId; }
    public void setIncidentId(UUID incidentId) { this.incidentId = incidentId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getSeverity() { return severity; }
    public void setSeverity(int severity) { this.severity = severity; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public UUID getReporterId() { return reporterId; }
    public void setReporterId(UUID reporterId) { this.reporterId = reporterId; }

    public int getPriorityScore() { return priorityScore; }
    public void setPriorityScore(int priorityScore) { this.priorityScore = priorityScore; }

    public Instant getReportedAt() { return reportedAt; }
    public void setReportedAt(Instant reportedAt) { this.reportedAt = reportedAt; }

    @Override
    public String toString() {
        return "EmergencyCreatedEvent{incidentId=" + incidentId +
               ", type=" + type + ", severity=" + severity +
               ", city=" + city + "}";
    }
}