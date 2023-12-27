package com.multiteam.modules.annotation.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record AnnotationDTO(

        @DateTimeFormat(pattern = "dd/MM/yyyy")
        @NotNull(message = "data da anotação não deve ser nula")
        LocalDate dateInitial,

        @NotBlank(message = "A anotação não de estar em branco")
        String annotation,

        @NotBlank(message = "A observação não de estar em branco")
        String observation,

        boolean sync
) {
}
