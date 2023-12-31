package com.multiteam.modules.program.repository;

import com.multiteam.modules.program.entity.FolderTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface FolderTreatmentRepository extends JpaRepository<FolderTreatment, UUID> {

    @Query("""
            select ft from FolderTreatment ft
            join ft.treatment tr on ft.treatment.id = tr.id
            join ft.folder fo on ft.folder.id = fo.id
            join fo.patient pa on pa.id = fo.patient.id
            where pa.id = :patientId
            and ft.treatment.situation = 'ANDAMENTO'
            and pa.active = true
            """)
    Set<FolderTreatment> getCardToCollectsResponsible(UUID patientId);
}
