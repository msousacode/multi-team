package com.multiteam.modules.program.repository;

import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.FolderTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FolderTreatmentRepository extends JpaRepository<FolderTreatment, UUID> {
}
