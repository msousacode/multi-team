package com.multiteam.user;

import com.multiteam.core.repositories.TenantableRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends TenantableRepository<User> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.tenantId = :tenantId")
    void updateTenantId(@Param("tenantId") UUID tenantId);

    /*
    Page<User> findByOwnerIdAndActiveIsTrue(UUID ownerId, Pageable pageable);

    Optional<User> findByIdAndOwnerId(UUID userId, UUID ownerId);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :userId AND u.ownerId = :ownerId")
    void inactiveUser(@Param("userId") UUID userId, @Param("ownerId") UUID ownerId);
     */
}
