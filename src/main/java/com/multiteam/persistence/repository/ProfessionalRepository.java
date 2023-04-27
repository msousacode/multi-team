package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {

    List<Professional> findAllByClinics_Id(UUID clinicId);

    @Modifying
    @Query("UPDATE Professional pr SET pr.active = false WHERE pr.id = :professionalId")
    void professionalInactive(@Param("professionalId") UUID professionalId);
}
