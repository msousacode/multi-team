package com.multiteam.modules.anamnese;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnamneseRepository extends TenantableRepository<Anamnese> {

    @Modifying
    @Query("UPDATE Anamnese a SET a.active = false WHERE a.id = :anamneseId AND a.tenantId = :tenantId")
    void inactiveAnamnese(UUID anamneseId, UUID tenantId);

    List<Anamnese> findAllByPatientId(UUID patientId);
}
