package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.projection.PatientsProfessionalsView;
import com.multiteam.persistence.types.SituationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findAllByClinic_Id(UUID clinicId);

    Optional<Patient> findByIdAndClinic_Id(UUID patientId, UUID clinicId);

    //TODO futuramente avaliar se essas querys podem ser um projection para unir as duas consultas em uma com propriedaes WHERE dinamicas.
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

    //TODO futuramente avaliar se essas querys podem ser um projection para unir as duas consultas em uma com propriedaes WHERE dinamicas.
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
            WHERE cli.id = :clinicId
            AND prs.situationType = :situation
            """)
    List<PatientsProfessionalsView> findAllPatientsByClinicId(
            @Param("clinicId") UUID clinicId,
            @Param("situation") SituationType situation);

    @Modifying
    @Query("UPDATE Patient pa SET pa.active = false WHERE pa.id = :patientId AND pa.clinic.id = :clinicId")
    void inactivePatient(@Param("patientId") UUID patientId, @Param("clinicId") UUID clinicId);
}
