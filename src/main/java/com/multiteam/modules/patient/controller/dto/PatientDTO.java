package com.multiteam.modules.patient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.treatment.Treatment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        String cpf,
        String treatmentObservation,
        UUID treatmentId,
        List<ProgramDTO> programs
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
                patient.getCpf(),
                patient.getTreatments().isEmpty() ? null : buildTreatment(patient.getTreatments().get(0)),
                patient.getTreatments().isEmpty() ? null : patient.getTreatments().get(0).getId(),
                List.of()
        );
    }

    public PatientDTO(Patient patient, List<Folder> folders) {
        this(
                patient.getId(),
                patient.getName(),
                patient.getUser().getEmail(),
                patient.getCellPhone(),
                patient.getSex().getDescription(),
                patient.getAge(),
                patient.getDateBirth(),
                patient.getCpf(),
                patient.getTreatments().isEmpty() ? null : buildTreatment(patient.getTreatments().get(0)),
                patient.getTreatments().isEmpty() ? null : patient.getTreatments().get(0).getId(),
                extractProgramas(folders)
        );
    }

    private static List<ProgramDTO> extractProgramas(List<Folder> folders) {
        List<ProgramDTO> programsDto = new ArrayList<>();

        for(Folder folder : folders) {
            programsDto = folder.getFolderPrograms().stream().map(program -> new ProgramDTO(program.getProgram())).toList();
        }
        return programsDto;
    }

    public static PatientDTO fromPatientDTO(Patient patient) {
        return new PatientDTO(patient);
    }

    private static String buildTreatment(Treatment treatment) {

        if (treatment != null) {

            return """
                    Paciente possui tratamento em %s com data ínicio %s até %s""".formatted(
                    treatment.getSituation(),
                    treatment.getInitialDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    treatment.getFinalDate() == null ? "Não informada" : treatment.getFinalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        return "";
    }
}
