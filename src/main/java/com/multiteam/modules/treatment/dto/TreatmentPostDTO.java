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
        List<Select> folders,
        LocalDate initialDate
) {
}
