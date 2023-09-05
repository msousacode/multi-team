package com.multiteam.modules.annotation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AnnotationDetailDTO(

    UUID treatmentId,
    LocalDate dateStart,
    LocalTime hourStart,
    LocalDate dateEnd,
    LocalTime hourEnd,
    String annotation,
    String observation,
    boolean sync,
    String status
) {

}
