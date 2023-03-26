package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Credential, UUID> {
}
