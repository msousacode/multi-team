package com.multiteam.modules.professional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.multiteam.modules.professional.dto.ProfessionalDTO;
import com.multiteam.modules.professional.dto.ProfessionalUseTreatmentRequest;
import com.multiteam.modules.professional.dto.ProfessionalUseTreatmentView;
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
    path = "/v1/professionals",
    produces = APPLICATION_JSON_VALUE
)
@RestController
public class ProfessionalController {

  private final ProfessionalService professionalService;

  public ProfessionalController(final ProfessionalService professionalService) {
    this.professionalService = professionalService;
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PostMapping
  public ResponseEntity<?> createProfessional(@RequestBody final ProfessionalDTO professionalDTO) {
    if (professionalService.createProfessional(professionalDTO)) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PutMapping
  public ResponseEntity<?> updateProfessional(@RequestBody final ProfessionalDTO professionalDTO) {
    if (professionalService.updateProfessional(professionalDTO)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_PROFESSIONAL_READ')")
  @GetMapping("/clinic/{clinicId}")
  public ResponseEntity<Page<ProfessionalDTO>> getAllProfessionals(
      @PathVariable("clinicId") final UUID clinicId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      @RequestParam(value = "sort", defaultValue = "createdDate") String sort,
      @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(professionalService.getAllProfessionals(clinicId,
            PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort))));
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_PROFESSIONAL_READ')")
  @GetMapping("/{professionalId}")
  public ResponseEntity<ProfessionalDTO> getProfessional(
      @PathVariable("professionalId") final UUID professionalId) {
    return professionalService.getProfessional(professionalId)
        .map(professional -> ResponseEntity.status(HttpStatus.OK).body(professional))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @DeleteMapping("/{professionalId}")
  public ResponseEntity<?> inactiveProfessional(
      @PathVariable("professionalId") final UUID professionalId) {
    if (professionalService.inactiveProfessional(professionalId)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
  @PostMapping("/use-treatments")
  public ResponseEntity<List<ProfessionalUseTreatmentView>> getProfessionalsUseTreatment(
      @RequestBody ProfessionalUseTreatmentRequest professionalUseTreatmentDTO) {
    return ResponseEntity.ok(
        professionalService.getProfessionalsUseTreatment(professionalUseTreatmentDTO));
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getProfessionalIdByUserId(@PathVariable("userId") final UUID userId) {
    return professionalService.getProfessionalByUserId(userId)
        .map(professional -> ResponseEntity.ok(
            professional.getId().toString())).orElse(ResponseEntity.notFound().build());
  }
}
