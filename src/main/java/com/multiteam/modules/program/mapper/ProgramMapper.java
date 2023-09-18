package com.multiteam.modules.program.mapper;

import com.multiteam.modules.program.Program;
import com.multiteam.modules.program.dto.ProgramDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgramMapper {

    ProgramMapper MAPPER = Mappers.getMapper(ProgramMapper.class);

    Program toEntity(ProgramDTO programDTO);

    ProgramDTO toDTO(Program programDTO);
}
