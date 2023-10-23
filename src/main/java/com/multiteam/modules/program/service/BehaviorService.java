package com.multiteam.modules.program.service;

import com.multiteam.core.enums.ApplicationError;
import com.multiteam.core.enums.TargetSituationEnum;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.modules.program.entity.Behavior;
import com.multiteam.modules.program.dto.BehaviorDTO;
import com.multiteam.modules.program.mapper.BehaviorMapper;
import com.multiteam.modules.program.repository.BehaviorRepository;
import com.multiteam.modules.program.repository.ProgramRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BehaviorService {

    private final BehaviorRepository behaviorRepository;
    private final ProgramRepository programRepository;

    public BehaviorService(BehaviorRepository behaviorRepository, ProgramRepository programRepository) {
        this.behaviorRepository = behaviorRepository;
        this.programRepository = programRepository;
    }

    @Transactional
    public Boolean createBehavior(UUID programId, BehaviorDTO behaviorDTO) {

        var program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ApplicationError.RESOURCE_NOT_FOUND.getMessage(), programId)));

        Behavior behavior = BehaviorMapper.MAPPER.toEntity(behaviorDTO);

        behavior.setProgram(program);
        behavior.setActive(true);
        behavior.setSituation(TargetSituationEnum.NAO_INICIADO.getValue());
        behaviorRepository.save(behavior);

        return Boolean.TRUE;
    }

    public List<Behavior> getAll(UUID programId) {
        return behaviorRepository.findAllByProgram_Id(programId);
    }

    @Transactional
    public Boolean updateBehavior(UUID behaviorId, BehaviorDTO behaviorDTO) {

        var optional = behaviorRepository.findById(behaviorId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ApplicationError.RESOURCE_NOT_FOUND.getMessage())));

        optional.setBehaviorName(behaviorDTO.behaviorName());
        optional.setObservation(behaviorDTO.observation());

        behaviorRepository.save(optional);

        return Boolean.TRUE;
    }

    @Transactional
    public void deleteBehavior(UUID behaviorId) {
        behaviorRepository.delete(behaviorId);
    }
}
