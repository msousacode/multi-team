package com.multiteam.clinic;

import com.multiteam.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClinicRespository extends JpaRepository<Clinic, UUID> {
}
