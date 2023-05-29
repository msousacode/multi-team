package com.multiteam.modules.patient;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends TenantableRepository<Patient> {

    @Modifying
    @Query("UPDATE Patient pa SET pa.active = false WHERE pa.id = :patientId AND pa.tenantId = :tenantId")
    void inactivePatient(UUID patientId, UUID tenantId);

    Page<Patient> findAllByNameContainingIgnoreCase(String patientName, Pageable pageable);
}
