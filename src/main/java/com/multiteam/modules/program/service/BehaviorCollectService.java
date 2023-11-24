package com.multiteam.modules.program.service;

import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.program.entity.BehaviorCollect;
import com.multiteam.modules.program.mapper.BehaviorCollectMapper;
import com.multiteam.modules.program.repository.BehaviorCollectRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public void createBehaviorCollect(List<UUID> programUUIDs, Patient patient) {
        List<BehaviorCollect> behaviorCollects = new ArrayList<>();


        var programs = programService.findProgramsByIdInBacth(programUUIDs);

        programs.forEach(program -> {

            program.getBehaviors().forEach(behavior -> {
                patient.getFolders().forEach(folder -> {
                    var behaviorCollect = BehaviorCollectMapper.MAPPER.toEntity(behavior);
                    behaviorCollect.setPatient(patient);
                    behaviorCollect.setBehavior(behavior);
                    behaviorCollect.setProgramId(program.getId());
                    behaviorCollect.setFolderId(folder.getId());
                    behaviorCollects.add(behaviorCollect);
                });
            });
        });

        behaviorCollectRepository.saveAll(behaviorCollects);
    }

    public List<BehaviorCollect> getCollectsByPatientId(UUID patientId, UUID folderId) {
        return behaviorCollectRepository.findAllByPatientIdAndFolderId(patientId, folderId);
    }
}
