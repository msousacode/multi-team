package com.multiteam.controller;

import com.multiteam.controller.dto.request.TreatmentRequest;
import com.multiteam.enums.RoleEnum;
import com.multiteam.persistence.entity.Treatment;
import com.multiteam.persistence.projection.TreatmentView;
import com.multiteam.service.TreatmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> createTreatment(@RequestBody TreatmentRequest treatmentRequest) {

        if (treatmentService.includeTreatment(treatmentRequest)) {
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
    public ResponseEntity<?> updateTreatment(@RequestBody TreatmentRequest treatmentRequest) {

        if(treatmentService.updateTreatment(treatmentRequest)){
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
