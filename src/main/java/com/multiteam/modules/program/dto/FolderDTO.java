package com.multiteam.modules.program.dto;

import com.multiteam.core.utils.Select;

import java.util.List;
import java.util.UUID;

public record FolderDTO(
        UUID programId,
        String folderName,
        List<Select> professionals
) {
}

