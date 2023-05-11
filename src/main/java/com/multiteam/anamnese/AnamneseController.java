package com.multiteam.anamnese;

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

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @PostMapping
    public ResponseEntity<?> createAnamnese(@RequestBody final AnamneseDTO anamneseDTO) {
        if (anamneseService.createAnamnese(anamneseDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @GetMapping("/{patientId}")
    public ResponseEntity<List<AnamneseDTO>> getAllAnamneses(@PathVariable("patientId") final UUID patientId) {
        return ResponseEntity.ok(anamneseService.getAllAnamneses(patientId));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @GetMapping("/{anamneseId}")
    public ResponseEntity<AnamneseDTO> getAnamnese(@PathVariable("anamneseId") final UUID anamneseId) {
        var anamneseOptional = anamneseService.getAnamnese(anamneseId);
        return anamneseOptional
                .map(anamneseDTO -> ResponseEntity.status(HttpStatus.OK).body(anamneseDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @DeleteMapping("/{anamneseId}")
    public ResponseEntity<?> inactiveAnamnese(@PathVariable("anamneseId") final UUID anamneseId) {
        if (anamneseService.inactiveAnamnese(anamneseId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
