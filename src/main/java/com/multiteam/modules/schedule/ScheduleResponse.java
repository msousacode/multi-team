package com.multiteam.modules.schedule;

import com.multiteam.modules.clinic.dto.ClinicDTO;
import com.multiteam.modules.patient.dto.PatientDTO;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record ScheduleResponse(
        PatientDTO patient,
        UUID professionalId,
        UUID id,
        String title,
        String start,
        String end,
        String url,
        String description,
        String color,
        Boolean active,
        String status,
        ClinicDTO clinic
) {
    private ScheduleResponse(Schedule schedule) {

        this(
                schedule.getPatient() != null ? PatientDTO.fromPatientDTO(schedule.getPatient()) : null,
                schedule.getProfessional().getId(),
                schedule.getId(),
                schedule.getTitle(),
                schedule.getStart().format(DateTimeFormatter.ISO_DATE_TIME),
                schedule.getEnd() != null ? schedule.getEnd().format(DateTimeFormatter.ISO_DATE_TIME) : null,
                schedule.getUrl(),
                schedule.getDescription(),
                schedule.getColor(),
                schedule.isActive(),
                schedule.getStatus().name(),
                ClinicDTO.createWithIdAndName(schedule.getClinic())
        );
    }

    public static ScheduleResponse fromScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(schedule);
    }
}
