package com.multiteam.controller;

import com.multiteam.controller.dto.PatientRequest;
import com.multiteam.controller.dto.ProfessionalRequest;
import com.multiteam.persistence.entity.Patient;
import com.multiteam.persistence.entity.Professional;
import com.multiteam.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/patients",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPatient(
            @RequestBody PatientRequest patientRequest,
            @PathVariable("clinicId") UUID clinicId) {

        if (patientService.createPatient(patientRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'SCHEDULE', 'PROFESSIONAL')")
    @GetMapping("/clinic/{clinicId}")
    public List<Patient> getAllPatient(@PathVariable("clinicId") UUID clinicId) {
        return patientService.getAllPatientsByClinicId(clinicId);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'SCHEDULE', 'PROFESSIONAL', 'PATIENT', 'GUEST')")
    @GetMapping("clinic/{clinicId}/patient/{patientId}")
    public ResponseEntity<Patient> getPatient(
            @PathVariable("patientId") UUID patientId,
            @PathVariable("clinicId") UUID clinicId) {

        var patientOptional = patientService.getPatientById(patientId, clinicId);
        return patientOptional
                .map(patient -> ResponseEntity.status(HttpStatus.OK).body(patient))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<?> updatePatient(@RequestBody PatientRequest patientRequest) {

        if(patientService.updatePatient(patientRequest)){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{patientId}/clinic/{clinicId}")
    public ResponseEntity<?> inactiveProfessional(
            @PathVariable("patientId") UUID patientId,
            @PathVariable("clinicId") UUID clinicId) {

        if(patientService.inactivePatient(patientId, clinicId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
