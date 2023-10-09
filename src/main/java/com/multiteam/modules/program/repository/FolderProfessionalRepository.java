package com.multiteam.modules.program.repository;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.program.entity.FolderProfessional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderProfessionalRepository extends JpaRepository<FolderProfessional, UUID> {

    @Modifying
    @Query(value = "update FolderProfessional pf set pf.situation = :situation where pf.folder.id in :folderIds")
    void updateSituationFolder(List<UUID> folderIds, SituationEnum situation);

    @Query(value = "select count(pf) from FolderProfessional pf where pf.folder.id = :folderId and pf.professional.id = :professionalId")
    Integer findByFolderIdAndProfessionalId(UUID folderId, UUID professionalId);

    List<FolderProfessional> findAllByFolder_Id(UUID id);
}
