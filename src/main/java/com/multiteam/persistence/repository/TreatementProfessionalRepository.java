package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.TreatmentProfessional;
import com.multiteam.persistence.types.SituationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TreatementProfessionalRepository extends JpaRepository<TreatmentProfessional, UUID> {

    @Modifying
    @Query("UPDATE TreatmentProfessional tp SET tp.situationType = :situation WHERE tp.treatment.id = :treatmentId")
    void inactiveProfessionalsByTreatmentId(@Param("treatmentId") UUID treatmentId, @Param("situation") SituationType situation);
}
