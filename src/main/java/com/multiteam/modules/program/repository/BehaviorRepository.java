package com.multiteam.modules.program.repository;

import com.multiteam.modules.program.entity.Behavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BehaviorRepository extends JpaRepository<Behavior, UUID> {

    List<Behavior> findAllByProgram_Id(UUID programId);

    @Modifying
    @Query("delete from Behavior b where b.id = :behaviorId")
    void delete(UUID behaviorId);
}
