package com.multiteam.modules.annotation.mapper;

import com.multiteam.modules.annotation.Annotation;
import com.multiteam.modules.annotation.dto.AnnotationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnotationMapper {

    AnnotationMapper MAPPER = Mappers.getMapper(AnnotationMapper.class);

    Annotation toEntity(AnnotationDTO dto);
}
