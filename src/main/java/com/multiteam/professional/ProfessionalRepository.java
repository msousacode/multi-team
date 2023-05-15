package com.multiteam.professional;

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
public interface ProfessionalRepository extends TenantableRepository<Professional> {

    @Query("""
            SELECT p FROM Professional p
            JOIN p.clinics c
            WHERE c.id = :clinicId
            AND p.active = true
            """)
    Page<Professional> findAllProfessionalsByClinicId(@Param("clinicId") final UUID clinicId, Pageable pageable);

    @Modifying
    @Query("UPDATE Professional pr SET pr.active = false WHERE pr.id = :professionalId AND pr.tenantId = :tenantId")
    void professionalInactive(@Param("professionalId") UUID professionalId, @Param("tenantId") final UUID tenantId);

    @Query("""
            SELECT p FROM Professional p
            JOIN p.clinics c
            WHERE c.id in (:clinics)
            AND p.active = true
            """)
    List<Professional> findAllByClinics_Id(List<UUID> clinics);

    @Query("""
            SELECT p FROM Professional p WHERE p.id in (:professionals)
            """)
    List<Professional> getAllProfessionalsByClinics(@Param("professionals") List<UUID> professionals);
}
