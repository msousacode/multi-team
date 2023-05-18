package com.multiteam.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface TenantableRepository<T> extends JpaRepository<T, UUID> {

    Optional<T> findOneById(UUID id);

    Page<T> findAllByTenantIdAndActiveIsTrue(UUID tenantId, Pageable pageable);
}