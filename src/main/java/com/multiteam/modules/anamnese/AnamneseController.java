package com.multiteam.modules.anamnese;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.multiteam.modules.anamnese.dto.AnamneseReportResponse;
import com.multiteam.modules.anamnese.dto.AnamneseRequest;
import com.multiteam.modules.anamnese.dto.AnamneseResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
    path = "/v1/anamneses",
    produces = APPLICATION_JSON_VALUE,
    consumes = APPLICATION_JSON_VALUE
)
@RestController
public class AnamneseController {


  private final AnamneseService anamneseService;

  public AnamneseController(final AnamneseService anamneseService) {
    this.anamneseService = anamneseService;
  }

  @PreAuthorize("hasAuthority('PERM_ANAMNESE_WRITE')")
  @PostMapping
  public ResponseEntity<?> createAnamnese(@RequestBody final AnamneseRequest anamneseDTO) {
    if (anamneseService.createAnamnese(anamneseDTO)) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_ANAMNESE_READ')")
  @GetMapping("/patient/{patientId}")
  public ResponseEntity<List<AnamneseResponse>> getAllAnamneses(
      @PathVariable("patientId") final UUID patientId) {
    return ResponseEntity.ok(anamneseService.getAllAnamneses(patientId));
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_ANAMNESE_READ')")
  @GetMapping("/{anamneseId}")
  public ResponseEntity<AnamneseReportResponse> getAnamneseReport(
      @PathVariable("anamneseId") final UUID anamneseId) {
    var anamneseOptional = anamneseService.getAnamneseReport(anamneseId);
    return anamneseOptional
        .map(anamnese -> ResponseEntity.status(HttpStatus.OK).body(anamnese))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @PreAuthorize("hasAuthority('PERM_ANAMNESE_WRITE')")
  @DeleteMapping("/{anamneseId}")
  public ResponseEntity<?> inactiveAnamnese(@PathVariable("anamneseId") final UUID anamneseId) {
    if (anamneseService.inactiveAnamnese(anamneseId)) {
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
