package com.multiteam.modules.patient;

import com.multiteam.core.repository.TenantableRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends TenantableRepository<Patient> {

  @Modifying
  @Query("UPDATE Patient pa SET pa.active = false WHERE pa.id = :patientId AND pa.tenantId = :tenantId")
  void inactivePatient(UUID patientId, UUID tenantId);

  Page<Patient> findAllByNameContainingIgnoreCaseAndActiveIsTrue(String patientName, Pageable pageable);

  @Query("""
      SELECT DISTINCT p FROM Patient p
      JOIN p.treatments t
      JOIN t.treatmentProfessionals tps
      WHERE tps.professional.id = :professionalId
      """)
  List<Patient> findAllTreatmentAndSituationProgressByProfessionalId(UUID professionalId);
}
