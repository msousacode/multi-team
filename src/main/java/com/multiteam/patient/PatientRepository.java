package com.multiteam.patient;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends TenantableRepository<Patient> {

    @Modifying
    @Query("UPDATE Patient pa SET pa.active = false WHERE pa.id = :patientId")
    void inactivePatient(@Param("patientId") UUID patientId);

    //Optional<Patient> findByIdAndOwnerId(UUID patientId, UUID ownerId);
}
