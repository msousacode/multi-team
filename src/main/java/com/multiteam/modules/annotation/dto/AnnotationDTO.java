package com.multiteam.modules.annotation.dto;

import java.util.List;

public record AnnotationDTO(
    List<AnnotationDetailDTO> annotations
) {
}
