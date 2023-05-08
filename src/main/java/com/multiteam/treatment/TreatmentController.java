package com.multiteam.treatment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/treatments",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_WRITE')")
    @PostMapping
    public ResponseEntity<?> createTreatment(@RequestBody TreatmentDTO treatmentDTO) {
        if (treatmentService.includeTreatment(treatmentDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_READ')")
    @GetMapping("/patient/{patientId}")
    public Set<Treatment> getAllTreatments(@PathVariable("patientId") UUID patientId) {
        return treatmentService.getAllTreatmentsByPatientId(patientId);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_READ')")
    @GetMapping("/{treatmentId}")
    public ResponseEntity<TreatmentView> getTreatment(@PathVariable("treatmentId") UUID treatmentId) {
        var treatmentOptional = treatmentService.getTreatmentById(treatmentId);
        return treatmentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateTreatment(@RequestBody TreatmentDTO treatmentDTO) {
        if(treatmentService.updateTreatment(treatmentDTO)){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_WRITE')" )
    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<?> inactiveTreatment(@PathVariable("treatmentId") UUID treatmentId) {

        if(treatmentService.inactiveTreatment(treatmentId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
