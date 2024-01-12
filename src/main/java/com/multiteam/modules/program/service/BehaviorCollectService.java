package com.multiteam.modules.program.service;

import com.multiteam.modules.program.entity.Behavior;
import com.multiteam.modules.program.entity.BehaviorCollect;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.Program;
import com.multiteam.modules.program.mapper.BehaviorCollectMapper;
import com.multiteam.modules.program.repository.BehaviorCollectRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BehaviorCollectService {

    private final BehaviorCollectRepository behaviorCollectRepository;
    private final ProgramService programService;

    public BehaviorCollectService(
            BehaviorCollectRepository behaviorCollectRepository,
            ProgramService programService) {
        this.behaviorCollectRepository = behaviorCollectRepository;
        this.programService = programService;
    }

    @Transactional
    public void createBehaviorCollect(List<UUID> programUUIDs, Folder folder) {
        List<BehaviorCollect> behaviorCollects = new ArrayList<>();

        var programs = programService.findProgramsByIdInBacth(programUUIDs);

        programs.forEach(program -> {

            program.getBehaviors().forEach(behavior -> {

                for (int i = 0; i < behavior.getOrderExecution(); i++) {
                    BehaviorCollect behaviorCollect = new BehaviorCollect();
                    if (!behavior.getResponsible()) {
                        /**Cria o comportamento para coleta do profissional*/
                        behaviorCollect = buildBehaviorCollect(folder, program, behavior, false);
                    } else {
                        /**Cria o comportamento para coleta do responsável*/
                        behaviorCollect = buildBehaviorCollect(folder, program, behavior, true);
                    }
                    behaviorCollects.add(behaviorCollect);
                }
            });
        });

        behaviorCollectRepository.saveAll(behaviorCollects);
    }

    private static BehaviorCollect buildBehaviorCollect(Folder folder, Program program, Behavior behavior, Boolean isResponsible) {
        var behaviorCollect = BehaviorCollectMapper.MAPPER.toEntity(behavior);
        behaviorCollect.setPatient(folder.getPatient());
        behaviorCollect.setBehavior(behavior);
        behaviorCollect.setProgramId(program.getId());
        behaviorCollect.setFolderId(folder.getId());
        behaviorCollect.setResponsible(isResponsible);
        return behaviorCollect;
    }

    public List<BehaviorCollect> getCollectsByPatientId(UUID patientId, UUID folderId, boolean isResponsable) {
        return behaviorCollectRepository.findAllByPatientIdAndFolderId(patientId, folderId, isResponsable);
    }

    public List<BehaviorCollect> findAllById(List<UUID> behaviorsUUIDs) {
        return behaviorCollectRepository.findAllById(behaviorsUUIDs);
    }

    @Transactional
    public void saveAll(List<BehaviorCollect> behaviorsCollect) {
        behaviorCollectRepository.saveAll(behaviorsCollect);
    }

    public List<BehaviorCollect> findBehaviorsCollects(UUID programId) {
        return behaviorCollectRepository.findAllByProgramId(programId);
    }

    public Integer findResponseCount(LocalDateTime collectionDate, String responseType) {
        return behaviorCollectRepository.findAllByCollectionDateAndResponse(collectionDate, responseType).size();
    }
}
