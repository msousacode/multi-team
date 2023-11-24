package com.multiteam.modules.program.repository;

import com.multiteam.modules.program.entity.BehaviorCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BehaviorCollectRepository extends JpaRepository<BehaviorCollect, UUID> {

    List<BehaviorCollect> findAllByPatient_Id(UUID patientId);
}
