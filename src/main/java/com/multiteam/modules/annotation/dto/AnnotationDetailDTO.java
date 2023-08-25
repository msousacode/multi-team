package com.multiteam.modules.annotation.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AnnotationDetailDTO(
    UUID treatmentId,
    LocalDate dateStart,
    String hourStart,
    LocalDate dateEnd,
    String hourEnd,
    String annotation,
    String observation,
    boolean sync,
    String status
) {

}
