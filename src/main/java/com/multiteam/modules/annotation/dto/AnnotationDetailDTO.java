package com.multiteam.modules.annotation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record AnnotationDetailDTO(
        @NotNull(message = "data da anotação não deve ser nula")
        LocalDate dateInitial,

        @NotBlank(message = "A anotação não de estar em branco")
        String annotation,

        @NotBlank(message = "A observação não de estar em branco")
        String observation,

        boolean sync,

        String status
) {
}
