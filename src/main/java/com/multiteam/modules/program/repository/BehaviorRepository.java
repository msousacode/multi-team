package com.multiteam.modules.program.repository;

import com.multiteam.modules.program.Behavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BehaviorRepository extends JpaRepository<Behavior, UUID> {
}
