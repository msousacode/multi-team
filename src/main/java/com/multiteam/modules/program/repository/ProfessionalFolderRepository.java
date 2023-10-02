package com.multiteam.modules.program.repository;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.program.entity.ProfessionalFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfessionalFolderRepository extends JpaRepository<ProfessionalFolder, UUID> {

    @Modifying
    @Query(value = "update ProfessionalFolder pf set pf.situation = :situation where pf.folder.id in :folderIds")
    void updateSituationFolder(List<UUID> folderIds, SituationEnum situation);
}
