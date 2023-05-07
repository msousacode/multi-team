package com.multiteam.professional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/professionals",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProfessional(@RequestBody ProfessionalDTO professionalRequest) {

        if (professionalService.createProfessional(professionalRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping
    public List<ProfessionalDTO> getAllProfessionals() {
        return professionalService.getAllProfessionals();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @GetMapping("/{professionalId}")
    public ResponseEntity<ProfessionalDTO> getProfessional(@PathVariable("professionalId") UUID professionalId) {
        return professionalService.getProfessional(professionalId)
                .map(professional -> ResponseEntity.status(HttpStatus.OK).body(professional))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateProfessional(@RequestBody ProfessionalDTO professionalDTO) {

       if(professionalService.updateProfessional(professionalDTO)){
           return ResponseEntity.status(HttpStatus.OK).build();
       } else {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @DeleteMapping("/{professionalId}")
    public ResponseEntity<?> inactiveProfessional(@PathVariable("professionalId") UUID professionalId) {

        if(professionalService.inactiveProfessional(professionalId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
