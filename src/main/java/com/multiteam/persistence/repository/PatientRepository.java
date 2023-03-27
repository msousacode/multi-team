package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findAllByClinic_Id(UUID clinicId);

    Optional<Patient> findByIdAndClinic_Id(UUID pacientId, UUID clinicId);
}
