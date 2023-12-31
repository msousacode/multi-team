package com.multiteam.modules.program.repository;

import com.multiteam.modules.program.entity.BehaviorCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BehaviorCollectRepository extends JpaRepository<BehaviorCollect, UUID> {

    @Query(value = "select bc from BehaviorCollect bc where bc.patient.id = :patientId and bc.folderId = :folderId and bc.responsible = :isResponsible")
    List<BehaviorCollect> findAllByPatientIdAndFolderId(UUID patientId, UUID folderId, boolean isResponsible);
}
