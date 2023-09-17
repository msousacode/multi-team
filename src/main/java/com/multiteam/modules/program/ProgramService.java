package com.multiteam.modules.program;

import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.repository.BehaviorRepository;
import com.multiteam.modules.program.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


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

        programRepository.save(program);

        return Boolean.TRUE;
    }

}
