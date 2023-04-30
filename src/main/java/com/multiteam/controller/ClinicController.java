package com.multiteam.controller;

import com.multiteam.controller.dto.request.ClinicRequest;
import com.multiteam.persistence.entity.Clinic;
import com.multiteam.service.ClinicService;
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
    public ResponseEntity<?> createClinic(@RequestBody ClinicRequest clinicRequest) {
        return (clinicService.createClinic(clinicRequest)) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public List<Clinic> getAllClinics(@PathVariable("userId") UUID userId) {
        return clinicService.getAllClinic(userId);
    }
}
