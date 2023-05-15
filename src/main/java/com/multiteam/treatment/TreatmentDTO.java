package com.multiteam.treatment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TreatmentDTO(
        UUID id,
        String observation,
        String situation,
        UUID patientId,
        List<UUID> professionals,
        List<UUID> clinics,
        LocalDate initialDate,
        LocalDate finalDate
) {
}
