package com.multiteam.modules.program.repository;

import com.multiteam.modules.program.entity.FolderProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FolderProgramRepository extends JpaRepository<FolderProgram, UUID> {

    void deleteByFolder_Id(UUID folderId);

    List<FolderProgram> findAllByFolder_Id(UUID folderId);
}
