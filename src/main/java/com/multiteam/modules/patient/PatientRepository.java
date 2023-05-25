package com.multiteam.modules.patient;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends TenantableRepository<Patient> {

    @Modifying
    @Query("UPDATE Patient pa SET pa.active = false WHERE pa.id = :patientId AND pa.tenantId = :tenantId")
    void inactivePatient(@Param("patientId") UUID patientId, @Param("tenantId") UUID tenantId);

    List<Patient> findByNameContainsIgnoreCase(@Param("patientName") String patientName);
}
