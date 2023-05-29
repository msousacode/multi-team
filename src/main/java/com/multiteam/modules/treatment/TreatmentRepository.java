package com.multiteam.modules.treatment;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreatmentRepository extends TenantableRepository<Treatment> {

    Page<Treatment> findAllByPatient_IdAndActiveIsTrue(UUID patientId, Pageable pageable);

    @Query("""
            SELECT t FROM Treatment t
            JOIN t.patient p
            WHERE p.id = :patientId
            """)
    List<Treatment> findAllTreatments(UUID patientId);

    @Modifying
    @Query("UPDATE Treatment tr SET tr.active = false WHERE tr.id = :treatmentId")
    void inactiveTreatment(UUID treatmentId);

    Page<Treatment> findAllByPatient_NameContainingIgnoreCase(String patientName, Pageable pageable);
}
