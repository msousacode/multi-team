package com.multiteam.modules.clinic;

import com.multiteam.modules.clinic.dto.ClinicDTO;
import com.multiteam.modules.clinic.dto.ClinicUseInCacheResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    path = "/v1/clinics",
    produces = APPLICATION_JSON_VALUE,
    consumes = APPLICATION_JSON_VALUE
)
@RestController
public class ClinicController {

  private final ClinicService clinicService;

  public ClinicController(final ClinicService clinicService) {
    this.clinicService = clinicService;
  }

  @PreAuthorize("hasRole('OWNER')")
  @PostMapping
  public ResponseEntity<?> createClinic(@RequestBody final ClinicDTO clinicDTO) {
    if (clinicService.createClinic(clinicDTO)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PutMapping
  public ResponseEntity<?> updateClinic(@RequestBody final ClinicDTO clinicDTO) {
    if (clinicService.updateClinic(clinicDTO)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
  @GetMapping
  public ResponseEntity<List<ClinicDTO>> getAllClinics() {
    return ResponseEntity.ok(clinicService.getAllClinic());
  }

  @GetMapping("/use-cache")
  public ResponseEntity<List<ClinicUseInCacheResponse>> getAllClinicsUseInCache() {
    return ResponseEntity.ok(clinicService.getAllClinicsUseInCache());
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
  @GetMapping("/{clinicId}")
  public ResponseEntity<ClinicDTO> getClinicById(@PathVariable("clinicId") final UUID clinicId) {
    var clinicOptional = clinicService.getClinicById(clinicId);
    return clinicOptional
        .map(clinic -> ResponseEntity.status(HttpStatus.OK).body(new ClinicDTO(clinic)))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}