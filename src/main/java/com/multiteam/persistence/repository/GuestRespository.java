package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Guest;
import com.multiteam.persistence.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuestRespository extends JpaRepository<Guest, UUID> {
}
