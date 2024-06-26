package com.multiteam.modules.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestRespository extends JpaRepository<Guest, UUID> {
    List<Guest> findAllByPatient_Id(UUID patientId);

    Optional<Guest> findByUser_Id(UUID userId);

    Optional<Guest> findGuestByEmail(String email);
}
