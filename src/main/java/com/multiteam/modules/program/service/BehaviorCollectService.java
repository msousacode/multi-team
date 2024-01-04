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

                    for(int i = 0; i <= behavior.getOrderExecution(); i++){

                        /**
                         * Cria o comportamento para coleta do profissional
                         */
                        var behaviorProfessional = BehaviorCollectMapper.MAPPER.toEntity(behavior);
                        behaviorProfessional.setPatient(patient);
                        behaviorProfessional.setBehavior(behavior);
                        behaviorProfessional.setProgramId(program.getId());
                        behaviorProfessional.setFolderId(folder.getId());
                        behaviorProfessional.setResponsible(false);

                        behaviorCollects.add(behaviorProfessional);

                        if(behavior.getResponsible()){
                            /**
                             * Cria o comportamento para coleta do responsável
                             */
                            var behaviorResponsible = BehaviorCollectMapper.MAPPER.toEntity(behavior);
                            behaviorResponsible.setPatient(patient);
                            behaviorResponsible.setBehavior(behavior);
                            behaviorResponsible.setProgramId(program.getId());
                            behaviorResponsible.setFolderId(folder.getId());
                            behaviorResponsible.setResponsible(true);

                            behaviorCollects.add(behaviorResponsible);
                        }
                    }
                });
            });
        });

        behaviorCollectRepository.saveAll(behaviorCollects);
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
}
