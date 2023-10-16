package com.multiteam.modules.treatment.dto;

import com.multiteam.core.utils.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TreatmentPostDTO(
        UUID id,
        String observation,
        String situation,
        LocalDate finalDate,
        LocalDate initialDate,
        List<Select> foldersAllocated,
        List<Select> foldersUnallocated
) {
}