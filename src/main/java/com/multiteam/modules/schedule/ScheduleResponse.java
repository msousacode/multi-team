package com.multiteam.modules.schedule;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record ScheduleResponse(
        //PatientDTO patient,
        //ClinicDTO client,
        UUID professionalId,
        UUID id,
        String title,
        String start,
        String end,
        String url,
        String description,
        String color,
        Boolean active,
        String status
) {
    private ScheduleResponse(Schedule schedule) {

        this(
                //PatientDTO.fromPatientDTO(schedule.getPatient()),
                //new ClinicDTO(schedule.getClinic()),
                schedule.getProfessional().getId(),
                schedule.getId(),
                schedule.getTitle(),
                schedule.getStart().format(DateTimeFormatter.ISO_DATE_TIME),
                schedule.getEnd() != null ? schedule.getEnd().format(DateTimeFormatter.ISO_DATE_TIME) : null,
                schedule.getUrl(),
                schedule.getDescription(),
                schedule.getColor(),
                schedule.isActive(),
                schedule.getStatus().name()
        );
    }

    public static ScheduleResponse fromScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(schedule);
    }
}
