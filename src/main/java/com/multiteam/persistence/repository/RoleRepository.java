package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Role;
import com.multiteam.persistence.types.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByRole(RoleType guest);
}
