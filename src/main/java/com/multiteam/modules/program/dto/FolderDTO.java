package com.multiteam.modules.program.dto;

import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.entity.Folder;

import java.util.List;
import java.util.UUID;

public record FolderDTO(
        UUID folderId,
        UUID patientId,
        String patientName,
        String folderName,
        List<Select> professionals,
        List<UUID> programs
) {
    public FolderDTO(Folder folder, List<Select> selects) {
        this(
                folder.getId(),
                folder.getPatient().getId(),
                folder.getPatient().getName(),
                folder.getFolderName(),
                selects,
                List.of()
        );
    }
}

