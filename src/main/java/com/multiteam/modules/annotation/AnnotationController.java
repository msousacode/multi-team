package com.multiteam.modules.annotation;

import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
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

    @PostMapping("/sync")
    public ResponseEntity<Void> syncAnnotations(@Valid @RequestBody List<AnnotationDTO> annotationDTO) {
        annotationService.syncAnnotations(annotationDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<TreatmentAnnotationDTO>> getAllAnnotations(
            @RequestBody final AnnototionSearch search) {
        return ResponseEntity.ok(annotationService.getAllAnnotations(search));
    }

    @DeleteMapping("/{annotationId}")
    public ResponseEntity<?> inactiveAnnotation(
            @PathVariable("annotationId") final UUID annotationId) {
        annotationService.inactiveAnnotation(annotationId);
        return ResponseEntity.ok().build();
    }
}

