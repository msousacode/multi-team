package com.multiteam.modules.program.dto;

import com.multiteam.core.utils.Select;

import java.util.List;

public record FolderGridDTO(
        List<Select> allocated,
        List<Select> unallocated
) {
}
