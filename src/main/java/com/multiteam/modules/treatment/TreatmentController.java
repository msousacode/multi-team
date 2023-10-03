package com.multiteam.modules.treatment;

import com.multiteam.modules.treatment.dto.TreatmentEditResponse;
import com.multiteam.modules.treatment.dto.TreatmentSearchDTO;
import com.multiteam.modules.treatment.dto.TreatmentPostDTO;
import com.multiteam.modules.treatment.dto.TreatmentListDTO;
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

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/treatments",
        produces = APPLICATION_JSON_VALUE
)
@RestController
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(final TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<?> createTreatment(@PathVariable("patientId") UUID patientId, @RequestBody final TreatmentPostDTO treatmentDTO) {
        if (treatmentService.createTreatment(patientId, treatmentDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<Page<TreatmentListDTO>> getAllTreatments(
            @RequestBody TreatmentSearchDTO search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(treatmentService.getAllTreatments(search,
                PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort))));
    }

    @GetMapping("/{treatmentId}")
    public ResponseEntity<TreatmentEditResponse> getTreatment(
            @PathVariable("treatmentId") UUID treatmentId) {
        return treatmentService.getTreatment(treatmentId)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Void> updateTreatment(@RequestBody final TreatmentPostDTO treatmentRequest) {
        if (treatmentService.updateTreatment(treatmentRequest)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> inactiveTreatment(@PathVariable("treatmentId") final UUID treatmentId) {
        if (treatmentService.inactiveTreatment(treatmentId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
