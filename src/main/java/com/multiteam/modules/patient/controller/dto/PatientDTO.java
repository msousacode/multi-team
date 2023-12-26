package com.multiteam.modules.patient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.program.dto.BehaviorDTO;
import com.multiteam.modules.program.dto.FolderListDTO;
import com.multiteam.modules.program.entity.Folder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PatientDTO(
        UUID id,
        String name,
        String email,
        String cellPhone,
        String sex,
        Integer age,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateBirth,
        List<FolderListDTO> folders,
        List<BehaviorDTO> behaviors
) {

    private PatientDTO(Patient patient) {
        this(
                patient.getId(),
                patient.getName(),
                patient.getUser().getEmail(),
                patient.getCellPhone(),
                patient.getSex().getDescription(),
                patient.getAge(),
                patient.getDateBirth(),
                List.of(),
                List.of()
        );
    }

    public PatientDTO(Patient patient, List<Folder> foldersList, List<BehaviorDTO> behaviors) {
        this(
                patient.getId(),
                patient.getName(),
                patient.getUser().getEmail(),
                patient.getCellPhone(),
                patient.getSex().getDescription(),
                patient.getAge(),
                patient.getDateBirth(),
                foldersList.stream().map(FolderListDTO::new).toList(),
                behaviors
        );
    }

    public static PatientDTO fromPatientDTO(Patient patient) {
        return new PatientDTO(patient);
    }
}
