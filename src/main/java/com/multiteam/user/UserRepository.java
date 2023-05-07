package com.multiteam.user;

import com.multiteam.core.annotations.DisableTenantFilter;
import com.multiteam.core.repositories.TenantableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends TenantableRepository<User> {

    Boolean existsByEmail(String email);

    @DisableTenantFilter
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.tenantId = :tenantId")
    void updateTenantId(@Param("tenantId") UUID tenantId);

    Page<User> findAllByTenantIdAndActiveIsTrue(UUID tenantId, Pageable pageable);

    Optional<User> findByTenantIdAndId(UUID tenantId, UUID userId);
    /*
    Page<User> findByOwnerIdAndActiveIsTrue(UUID ownerId, Pageable pageable);

    Optional<User> findByIdAndOwnerId(UUID userId, UUID ownerId);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :userId AND u.ownerId = :ownerId")
    void inactiveUser(@Param("userId") UUID userId, @Param("ownerId") UUID ownerId);
     */
}
