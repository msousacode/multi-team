package com.multiteam.user;

import com.multiteam.core.annotation.DisableTenantFilter;
import com.multiteam.core.repository.TenantableRepository;
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
    @Query("UPDATE User u SET u.tenantId = :tenantId WHERE u.id = :userId")
    void updateTenantIdMyUser(@Param("tenantId") UUID tenantId, @Param("userId") UUID userId);

    Page<User> findAllByTenantIdAndActiveIsTrue(UUID tenantId, Pageable pageable);

    Optional<User> findByTenantIdAndId(UUID tenantId, UUID userId);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :userId AND u.tenantId = :tenantId")
    void inactiveUser(@Param("userId") UUID userId, @Param("tenantId") UUID tenantId);

    @Modifying
    @Query("UPDATE User u SET u.password = :password1 WHERE u.id = :userId AND u.tenantId = :tenantId")
    void updatePassword(@Param("userId") UUID userId, @Param("password1") String password1, @Param("tenantId") UUID tenantId);
}
