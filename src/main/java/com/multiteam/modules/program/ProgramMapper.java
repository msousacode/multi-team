package com.multiteam.modules.program;

import com.multiteam.modules.program.dto.ProgramDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgramMapper {

    ProgramMapper MAPPER = Mappers.getMapper(ProgramMapper.class);

    Program toEntity(ProgramDTO programDTO);

    ProgramDTO toDTO(Program programDTO);
}
