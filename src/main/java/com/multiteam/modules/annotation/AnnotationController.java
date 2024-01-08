package com.multiteam.modules.annotation;

import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnotationListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
@RestController
@RequestMapping(path = "/v1/annotations", produces = APPLICATION_JSON_VALUE)
@Validated
public class AnnotationController {

    private final AnnotationService annotationService;

    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @PreAuthorize("hasRole('GUEST')")
    @PostMapping("/sync")
    public ResponseEntity<Void> syncAnnotations(@Valid @RequestBody List<AnnotationDTO> annotationDTO) {
        annotationService.syncAnnotations(annotationDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{annotationId}")
    public ResponseEntity<?> inactiveAnnotation(
            @PathVariable("annotationId") final UUID annotationId) {
        annotationService.inactiveAnnotation(annotationId);
        return ResponseEntity.ok().build();
    }

    /*
    @GetMapping("/treatment/{treatmentId}")
    public ResponseEntity<Page<AnnotationOfTreatment>> find(@PathVariable UUID treatmentId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "100") int size,
                                                            @RequestParam(value = "sort", defaultValue = "name") String sort,
                                                            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(annotationService.findAnnotations(treatmentId,PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort))));
    }*/

    @GetMapping("/treatment/{treatmentId}")
    public ResponseEntity<List<AnnotationListDTO>> find(@PathVariable UUID treatmentId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(annotationService.findAnnotations(treatmentId));
    }
}

