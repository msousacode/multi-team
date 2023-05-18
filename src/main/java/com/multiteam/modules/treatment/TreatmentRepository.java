package com.multiteam.modules.treatment;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreatmentRepository extends TenantableRepository<Treatment> {

    Page<Treatment> findAllByPatient_IdAndActiveIsTrue(@Param("patientId") UUID patientId, Pageable pageable);

    @Query("""
            SELECT t FROM Treatment t
            JOIN t.patient p
            WHERE p.id = :patientId
            """)
    List<Treatment> findAllTreatments(@Param("patientId") UUID patientId);

    /*
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
    */

    @Modifying
    @Query("UPDATE Treatment tr SET tr.active = false WHERE tr.id = :treatmentId")
    void inactiveTreatment(@Param("treatmentId") UUID treatmentId);
}
