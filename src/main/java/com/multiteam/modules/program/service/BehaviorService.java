package com.multiteam.modules.program.service;

import com.multiteam.core.enums.ApplicationError;
import com.multiteam.core.enums.TargetSituationEnum;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.modules.program.dto.BehaviorDTO;
import com.multiteam.modules.program.dto.BehaviorResponseCollectDTO;
import com.multiteam.modules.program.entity.Behavior;
import com.multiteam.modules.program.entity.BehaviorCollect;
import com.multiteam.modules.program.mapper.BehaviorMapper;
import com.multiteam.modules.program.repository.BehaviorRepository;
import com.multiteam.modules.program.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class BehaviorService {

    private final BehaviorRepository behaviorRepository;
    private final BehaviorCollectService behaviorCollectService;
    private final ProgramRepository programRepository;

    public BehaviorService(
            BehaviorRepository behaviorRepository,
            BehaviorCollectService behaviorCollectService,
            ProgramRepository programRepository) {
        this.behaviorRepository = behaviorRepository;
        this.programRepository = programRepository;
        this.behaviorCollectService = behaviorCollectService;
    }

    @Transactional
    public Boolean createBehavior(UUID programId, BehaviorDTO behaviorDTO) {

        var program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ApplicationError.RESOURCE_NOT_FOUND.getMessage(), programId)));

        Behavior behavior = BehaviorMapper.MAPPER.toEntity(behaviorDTO);

        behavior.setProgram(program);
        behavior.setActive(true);
        behavior.setSituation(TargetSituationEnum.NAO_INICIADO.getValue());
        behavior.setResponsible(behaviorDTO.responsible());
        behavior.setTime(behaviorDTO.time());

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
        optional.setQuestion(behaviorDTO.question());
        optional.setTime(behaviorDTO.time());

        behaviorRepository.save(optional);

        return Boolean.TRUE;
    }

    @Transactional
    public void deleteBehavior(UUID behaviorId) {
        behaviorRepository.delete(behaviorId);
    }

    @Transactional
    public boolean syncBehaviorsCollect(List<BehaviorResponseCollectDTO> behaviorsDTOs) {

        List<UUID> behaviorsUUIDs = behaviorsDTOs.stream().map(BehaviorResponseCollectDTO::behaviorId).toList();

        List<BehaviorCollect> behaviorsCollect = behaviorCollectService.findAllById(behaviorsUUIDs);

        behaviorsDTOs.forEach(behaviorResponseCollectDTO -> {
            behaviorsCollect.forEach(behavior -> {
                if(behaviorResponseCollectDTO.behaviorId().equals(behavior.getId()))
                    behavior.setResponse(behaviorResponseCollectDTO.response());
            });
        });

        behaviorCollectService.saveAll(behaviorsCollect);

        return true;
    }
}
