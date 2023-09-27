package com.multiteam.modules.program.service;

import com.multiteam.core.enums.AbilityEnum;
import com.multiteam.core.enums.ProtocolEnum;
import com.multiteam.modules.program.Program;
import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.mapper.ProgramMapper;
import com.multiteam.modules.program.repository.BehaviorRepository;
import com.multiteam.modules.program.repository.ProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final BehaviorRepository behaviorRepository;

    public ProgramService(ProgramRepository programRepository, BehaviorRepository behaviorRepository) {
        this.programRepository = programRepository;
        this.behaviorRepository = behaviorRepository;
    }

    @Transactional
    public boolean createProgram(ProgramDTO programDTO) {

        Program program = ProgramMapper.MAPPER.toEntity(programDTO);

        program.setAbility(AbilityEnum.get(program.getAbility()).getValue());
        program.setProtocol(ProtocolEnum.get(program.getProtocol()).getValue());
        program.setActive(true);

        programRepository.save(program);

        return Boolean.TRUE;
    }

    public Page<Program> getAll(Pageable pageable) {
        return programRepository.findAll(pageable);
    }

    public Optional<Program> getById(UUID programId) {
        return programRepository.findById(programId);
    }

    @Transactional
    public Boolean updateProgram(ProgramDTO programDTO) {

        var optional = getById(programDTO.programId());

        if(optional.isPresent()) {
            var result = ProgramMapper.MAPPER.toEntity(programDTO);
            result.setId(programDTO.programId());
            result.setActive(true);
            programRepository.save(result);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}