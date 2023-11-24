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

        programUUIDs.forEach(programId -> {

            var program = programService.findProgramById(programId);

            program.get().getBehaviors().forEach(behavior -> {
                var behaviorCollect = BehaviorCollectMapper.MAPPER.toEntity(behavior);
                behaviorCollect.setPatient(patient);
                behaviorCollect.setBehavior(behavior);
                behaviorCollect.setProgramId(programId);
                behaviorCollects.add(behaviorCollect);
            });
        });

        behaviorCollectRepository.saveAll(behaviorCollects);
    }

    public List<BehaviorCollect> getCollectsByPatientId(UUID patientId) {
        return behaviorCollectRepository.findAllByPatient_Id(patientId);
    }
}
