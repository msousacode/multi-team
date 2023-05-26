package com.multiteam.modules.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/schedules",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_SCHEDULE_READ', 'PERM_SCHEDULE_WRITE')")
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody final ScheduleRequest scheduleRequest) {
        if (scheduleService.createSchedule(scheduleRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/clinic/{clinicId}")
    public List<ScheduleResponse> getAllById(@PathVariable("clinicId") final UUID clinicId) {
        return scheduleService.getAllById(clinicId);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_SCHEDULE_READ', 'PERM_SCHEDULE_WRITE')")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable("scheduleId") final UUID scheduleId) {
        return scheduleService.getSchedule(scheduleId)
                .map(schedule -> ResponseEntity.ok().body(schedule))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_SCHEDULE_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateSchedule(@RequestBody final ScheduleRequest scheduleRequest) {
        if (scheduleService.updateSchedule(scheduleRequest)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_SCHEDULE_WRITE')")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> inactiveSchedule(@PathVariable("scheduleId") final UUID scheduleId) {
        scheduleService.inactiveSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
}
