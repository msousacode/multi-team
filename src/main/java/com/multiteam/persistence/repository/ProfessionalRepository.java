package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {
    List<Professional> findAllByClinic_Id(UUID clinicId);
}
