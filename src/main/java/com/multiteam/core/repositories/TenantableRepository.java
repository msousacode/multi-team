package com.multiteam.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface TenantableRepository<T> extends JpaRepository<T, UUID> {

    Optional<T> findOneById(UUID id);
}