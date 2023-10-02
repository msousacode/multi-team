package com.multiteam.modules.program.dto;

import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.entity.Folder;

import java.util.List;
import java.util.UUID;

public record FolderListDTO(
        UUID folderId,
        String folderName,
        Boolean active,
        String patientName,
        List<Select> professionals
) {
    public FolderListDTO(Folder folder) {
        this(folder.getId(), folder.getFolderName(), folder.getActive(), folder.getPatient().getName(), List.of());
    }
}

