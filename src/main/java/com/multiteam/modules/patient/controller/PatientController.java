package com.multiteam.modules.patient.controller;

import com.multiteam.modules.patient.service.PatientService;
import com.multiteam.modules.patient.controller.dto.PatientDTO;
import com.multiteam.modules.patient.controller.dto.PatientFilter;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/v1/patients")
@RestController
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
public class PatientController {

    private final PatientService patientService;

    public PatientController(final PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody PatientDTO patientDTO) {
        if (patientService.createPatient(patientDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updatePatient(@RequestBody PatientDTO patientDTO) {
        if (patientService.updatePatient(patientDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
            @RequestBody final PatientFilter patientFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(patientService.findAllTreatmentAndSituationProgressByProfessionalId(patientFilter,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort))));
    }

    @GetMapping("{patientId}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable("patientId") final UUID patientId) {
        var optionalPatient = patientService.getPatientById(patientId).map(PatientDTO::fromPatientDTO);
        return optionalPatient
                .map(patient -> ResponseEntity.status(HttpStatus.OK).body(patient))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> inactivePatient(@PathVariable("patientId") final UUID patientId) {
        if (patientService.inactivePatient(patientId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/professional/{professionalId}/mobile")
    public ResponseEntity<List<PatientDTO>> findPatientsInTreatment(@PathVariable("professionalId") @NotNull UUID professionalId) {
        var patients = patientService.findPatientsInTreatment(professionalId);
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }
}