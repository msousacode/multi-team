package com.multiteam.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_PATIENT_WRITE')")
    @PostMapping
    public ResponseEntity<?> createPatient(
            @RequestBody PatientDTO patientRequest) {
        if (patientService.createPatient(patientRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_PATIENT_READ')")
    @GetMapping
    public ResponseEntity<Page<PatientDTO>> getAllPatientByOwnerId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getAllPatients(PageRequest.of(page, size)));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_PATIENT_READ')")
    @GetMapping("{patientId}/owner/{ownerId}")
    public ResponseEntity<PatientDTO> getPatient(
            @PathVariable("patientId") UUID patientId,
            @PathVariable("ownerId") UUID ownerId) {

        var result = patientService.getPatient(patientId, ownerId).map(PatientDTO::fromPatientDTO);
        return result
                .map(patient -> ResponseEntity.status(HttpStatus.OK).body(patient))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_PATIENT_WRITE')")
    @PutMapping
    public ResponseEntity<?> updatePatient(@RequestBody PatientDTO patientDTO) {

        if(patientService.updatePatient(patientDTO)){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAnyAuthority('PERM_PATIENT_WRITE')")
    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> inactivePatient(
            @PathVariable("patientId") UUID patientId) {

        if(patientService.inactivePatient(patientId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
