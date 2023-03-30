package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Treatment;
import com.multiteam.persistence.projection.TreatmentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
            p.name AS patientName,
            p.middleName AS patientMiddleName,
            pf.name AS professionalName,
            pf.middleName AS professionalMiddleName
            FROM Treatment t
            JOIN t.guests tg
            JOIN t.patient p
            JOIN t.treatmentProfessionals tp
            JOIN tp.professional pf
            WHERE tg.id = :guestId
            """)
    List<TreatmentView> getAllTreatmentsByGuestId(@Param("guestId") UUID guestId);
}
