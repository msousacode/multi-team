package com.multiteam.modules.schedule;

import java.time.format.DateTimeFormatter;

public record ScheduleResponse(
        //PatientDTO patient,
        //ClinicDTO client,
        //ProfessionalDTO professional,
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
                //ProfessionalDTO.fromProfessionalDTO(schedule.getProfessional()),
                schedule.getTitle(),
                schedule.getStart().format(DateTimeFormatter.ISO_DATE_TIME),
                schedule.getEnd() != null ? schedule.getEnd().format(DateTimeFormatter.ISO_DATE_TIME) : null,
                schedule.getUrl(),
                schedule.getDescription(),
                schedule.getColor(),
                schedule.isActive(),
                schedule.getStatus().getColor()
        );
    }

    public static ScheduleResponse fromScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(schedule);
    }
}
