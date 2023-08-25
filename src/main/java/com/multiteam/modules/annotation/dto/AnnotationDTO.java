package com.multiteam.modules.annotation.dto;

import java.util.List;
import java.util.UUID;

public record AnnotationDTO(
    UUID treatmentId,
    UUID patientId,
    List<AnnotationDetailDTO> annotations
) {
}
