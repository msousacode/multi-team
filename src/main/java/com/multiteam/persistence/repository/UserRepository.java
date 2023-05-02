package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.ownerId = :ownerId")
    void updateOwnerId(@Param("ownerId") UUID ownerId);

    Page<User> findByOwnerIdAndActiveIsTrue(UUID ownerId, Pageable pageable);

    Optional<User> findByIdAndOwnerId(UUID userId, UUID ownerId);
}
