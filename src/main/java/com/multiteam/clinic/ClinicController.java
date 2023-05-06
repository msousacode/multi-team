package com.multiteam.clinic;

import com.multiteam.clinic.dto.ClinicDTO;
import org.hibernate.mapping.Any;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/clinics",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<?> createClinic(@RequestBody ClinicDTO clinicRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicService.createClinic(clinicRequest));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateClinic(@RequestBody ClinicDTO clinicRequest) {
        return clinicService.updateClinic(clinicRequest) ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClinicDTO>> getAllClinics() {
        return ResponseEntity.ok(clinicService.getAllClinic());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/{clinicId}")
    public ResponseEntity<Clinic> getClinic(@PathVariable("clinicId") UUID clinicId) {
        return clinicService.getClinicById(clinicId)
                .map(clinic -> ResponseEntity.status(HttpStatus.OK).body(clinic))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
