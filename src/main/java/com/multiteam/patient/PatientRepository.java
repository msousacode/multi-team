package com.multiteam.patient;

import com.multiteam.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Page<Patient> findAllByOwnerIdAndActiveIsTrue(UUID ownerId, Pageable pageable);

/*
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
            @Param("situation") SituationEnum situation);
    */
/*
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
            @Param("situation") SituationEnum situation);
 */

    @Modifying
    @Query("UPDATE Patient pa SET pa.active = false WHERE pa.id = :patientId AND pa.ownerId = :ownerId")
    void inactivePatient(@Param("patientId") UUID patientId, @Param("ownerId") UUID ownerId);

    Optional<Patient> findByIdAndOwnerId(UUID patientId, UUID ownerId);
}
