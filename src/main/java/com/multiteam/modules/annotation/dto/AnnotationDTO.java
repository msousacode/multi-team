package com.multiteam.modules.annotation.dto;

import com.multiteam.modules.annotation.Annotation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AnnotationDTO(
    UUID treatmentId,
    String annotation,
    String observation,
    String dateStart,
    String hourStart,
    String dateEnd,
    String hourEnd
) {

  public AnnotationDTO(Annotation annotation) {
    this(
        annotation.getId(),
        annotation.getAnnotation(),
        annotation.getObservation(),
        null,
        null,
        null,
        null
    );
  }
}
