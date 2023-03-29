package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, UUID> {
    Set<Treatment> findAllByPatient_Id(UUID patientId);
}
