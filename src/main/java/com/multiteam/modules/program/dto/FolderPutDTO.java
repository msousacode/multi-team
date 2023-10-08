package com.multiteam.modules.program.dto;

import com.multiteam.core.utils.Select;

import java.util.List;
import java.util.UUID;

public record FolderPutDTO(
        String patientName,
        String folderName,
        List<Select> professionals,
        List<UUID> programs
) {
}

