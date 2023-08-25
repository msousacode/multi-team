package com.multiteam.modules.patient;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.multiteam.modules.patient.dto.PatientDTO;
import com.multiteam.modules.patient.dto.PatientFilter;
import java.util.List;
import java.util.UUID;
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

@RequestMapping(
    path = "/v1/patients",
    produces = APPLICATION_JSON_VALUE,
    consumes = APPLICATION_JSON_VALUE
)
@RestController
public class PatientController {

  private final PatientService patientService;

  public PatientController(final PatientService patientService) {
    this.patientService = patientService;
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_PATIENT_WRITE')")
  @PostMapping
  public ResponseEntity<?> createPatient(
      @RequestBody final PatientDTO patientDTO) {
    if (patientService.createPatient(patientDTO)) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_PATIENT_WRITE')")
  @PutMapping
  public ResponseEntity<?> updatePatient(@RequestBody final PatientDTO patientDTO) {
    if (patientService.updatePatient(patientDTO)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_PATIENT_READ')")
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

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_PATIENT_READ')")
  @GetMapping("{patientId}")
  public ResponseEntity<PatientDTO> getPatient(@PathVariable("patientId") final UUID patientId) {
    var optionalPatient = patientService.findOneById(patientId).map(PatientDTO::fromPatientDTO);
    return optionalPatient
        .map(patient -> ResponseEntity.status(HttpStatus.OK).body(patient))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_PATIENT_WRITE')")
  @DeleteMapping("/{patientId}")
  public ResponseEntity<?> inactivePatient(@PathVariable("patientId") final UUID patientId) {
    if (patientService.inactivePatient(patientId)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL') or hasAuthority('PERM_PATIENT_READ')")
  @GetMapping("/professional/{professionalId}")
  public ResponseEntity<List<PatientDTO>> getAllPatients(@PathVariable("professionalId") final UUID professionalId) {
    var patients = patientService.findAllTreatmentAndSituationProgressByProfessionalId(professionalId).stream().map(PatientDTO::fromPatientDTO).toList();
    return ResponseEntity.status(HttpStatus.OK).body(patients);
  }
}
