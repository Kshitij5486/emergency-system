package com.emergency.emergencyservice.repository;

import com.emergency.emergencyservice.entity.Incident;
import com.emergency.emergencyservice.entity.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    List<Incident> findByReporterId(UUID reporterId);
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByCityAndStatus(String city, IncidentStatus status);
}
