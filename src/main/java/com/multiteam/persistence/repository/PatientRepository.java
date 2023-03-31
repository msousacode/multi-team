package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.projection.PatientsProfessionalsView;
import com.multiteam.persistence.types.SituationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findAllByClinic_Id(UUID clinicId);

    Optional<Patient> findByIdAndClinic_Id(UUID pacientId, UUID clinicId);

    @Query("""
            SELECT DISTINCT
            cli.clinicName AS clinicName,
            pet.id AS patientId,
            pet.name AS name,
            pet.middleName AS middleName,
            pro.name AS professionalName,
            pro.middleName AS professionalMiddleName,
            prs.situationType AS situation
            FROM Patient pet
            JOIN pet.clinic cli
            JOIN cli.professionals pro
            JOIN pro.professionals prs
            WHERE pro.id = :professionalId
            AND prs.situationType = :situation
            """)
    List<PatientsProfessionalsView> findAllPatientsByProfessionalId(
            @Param("professionalId") UUID professionalId,
            @Param("situation") SituationType situation);
}
