package com.multiteam.treatment;

import com.multiteam.treatment.dto.TreatmentFilter;
import com.multiteam.treatment.dto.TreatmentRequest;
import com.multiteam.treatment.dto.TreatmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    public TreatmentController(final TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_WRITE')")
    @PostMapping
    public ResponseEntity<?> createTreatment(@RequestBody final TreatmentRequest treatmentDTO) {
        if (treatmentService.createTreatment(treatmentDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_READ')")
    @PostMapping("/filter")
    public ResponseEntity<Page<TreatmentResponse>> getAllTreatments(
            @RequestBody TreatmentFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(treatmentService.getAllTreatments(filter, PageRequest.of(page, size)));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_READ')")
    @GetMapping("/{treatmentId}")
    public ResponseEntity<TreatmentResponse> getTreatment(@PathVariable("treatmentId") UUID treatmentId) {
        var treatmentOptional = treatmentService.getTreatment(treatmentId);
        return treatmentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateTreatment(@RequestBody TreatmentRequest treatmentDTO) {
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
