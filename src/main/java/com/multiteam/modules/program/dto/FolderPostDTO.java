package com.multiteam.modules.program.dto;

import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.Program;

import java.util.List;
import java.util.UUID;

public record FolderPostDTO(
        UUID folderId,
        UUID patientId,
        String patientName,
        String folderName,
        List<Select> professionals,
        List<UUID> programs
) {
    public FolderPostDTO(Folder folder, List<Select> selects, List<UUID> programs) {
        this(
                folder.getId(),
                folder.getPatient().getId(),
                folder.getPatient().getName(),
                folder.getFolderName(),
                selects,
                programs
        );
    }
}

