package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.TreatmentProfessional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TreatementProfessionalRepository extends JpaRepository<TreatmentProfessional, UUID> {

    @Modifying
    void deleteByTreatment_Id(UUID treatmentId);
}
