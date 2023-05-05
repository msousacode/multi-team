package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Role;
import com.multiteam.core.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByRole(RoleEnum guest);
}
