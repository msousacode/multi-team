package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClinicRespository extends JpaRepository<Clinic, UUID> {

    List<Clinic> findAllByUserId(UUID userId);
}
