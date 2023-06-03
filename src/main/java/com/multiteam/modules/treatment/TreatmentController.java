package com.multiteam.modules.treatment;

import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationRequest;
import com.multiteam.modules.treatment.dto.TreatmentEditResponse;
import com.multiteam.modules.treatment.dto.TreatmentFilter;
import com.multiteam.modules.treatment.dto.TreatmentProfessionalAnnotationDTO;
import com.multiteam.modules.treatment.dto.TreatmentRequest;
import com.multiteam.modules.treatment.dto.TreatmentResponse;
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

import java.util.List;
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
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(treatmentService.getAllTreatments(filter, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort))));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_READ')")
    @GetMapping("/{treatmentId}")
    public ResponseEntity<TreatmentEditResponse> getTreatment(@PathVariable("treatmentId") UUID treatmentId) {
        return treatmentService.getTreatment(treatmentId)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateTreatment(@RequestBody final TreatmentRequest treatmentRequest) {
        if (treatmentService.updateTreatment(treatmentRequest)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_TREATMENT_WRITE')")
    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<?> inactiveTreatment(@PathVariable("treatmentId") final UUID treatmentId) {
        if (treatmentService.inactiveTreatment(treatmentId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/annotations")
    public ResponseEntity<Void> includeAnnotation(@RequestBody final TreatmentAnnotationRequest treatmentAnnotationRequest) {
        treatmentService.includeAnnotation(treatmentAnnotationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/patient/{patientId}/annotations")
    public ResponseEntity<List<TreatmentAnnotationDTO>> getAnnotations(@PathVariable("patientId") final UUID patientId) {
        return ResponseEntity.ok(treatmentService.getallAnnotations(patientId));
    }

    @GetMapping("/annotations/{treatmentProfessionalId}")
    public ResponseEntity<TreatmentProfessionalAnnotationDTO> getAnnotationsById(@PathVariable("treatmentProfessionalId") final UUID treatmentProfessionalId) {
        return ResponseEntity.ok(treatmentService.getAnnotationsById(treatmentProfessionalId));
    }
}
