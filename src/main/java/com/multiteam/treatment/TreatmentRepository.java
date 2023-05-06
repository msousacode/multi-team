package com.multiteam.treatment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, UUID> {
    Set<Treatment> findAllByPatient_Id(UUID patientId);

    @Query("""
            SELECT
            t.id AS id,
            t.situation AS situation,
            t.treatmentType AS treatmentType,
            p.name AS patientName
            FROM Treatment t
            JOIN t.guests tg
            JOIN t.patient p
            JOIN t.treatmentProfessionals tp
            JOIN tp.professional pf
            WHERE tg.id = :guestId
            """)
    List<TreatmentView> getAllTreatmentsByGuestId(@Param("guestId") UUID guestId);

    @Modifying
    @Query("UPDATE Treatment tr SET tr.active = false WHERE tr.id = :treatmentId")
    void inactiveTreatment(@Param("treatmentId") UUID treatmentId);

    @Query("""
           SELECT
           t.id AS id,
           t.description AS description,
           t.treatmentType AS treatmentType,
           t.situation AS situation,
           t.initialDate AS initialDate,
           t.finalDate AS finalDate,
           t.active AS active
           FROM Treatment t
           WHERE t.id = :treatmentId
           """)
    Optional<TreatmentView> findByIdProjection(@Param("treatmentId") UUID treatmentId);
}
