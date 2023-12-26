package com.multiteam.modules.annotation;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/annotations", produces = APPLICATION_JSON_VALUE)
@Validated
public class AnnotationController {

    private final AnnotationService annotationService;

    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @PreAuthorize("hasRole('PROFESSIONAL')")
    @PostMapping("/treatment/{treatmentId}/mobile")
    public ResponseEntity<Void> syncAnnotations(@PathVariable("treatmentId") @NotNull UUID treatmentId, @Valid @RequestBody AnnotationDTO annotationDTO) {
        annotationService.syncAnnotations(annotationDTO, treatmentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('PROFESSIONAL')")
    @PutMapping
    public ResponseEntity<Void> upddateAnnotation(@RequestBody AnnotationDTO annotationDTO) {
        return annotationService.updateAnnotation(annotationDTO) ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @PostMapping("/search")
    public ResponseEntity<List<TreatmentAnnotationDTO>> getAllAnnotations(
            @RequestBody final AnnototionSearch search) {
        return ResponseEntity.ok(annotationService.getAllAnnotations(search));
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @GetMapping("/{treatmentProfessionalId}")
    public ResponseEntity<AnnotationDTO> getAnnotation(
            @PathVariable("treatmentProfessionalId") final UUID treatmentProfessionalId) {
        return ResponseEntity.ok(annotationService.getAnnotation(treatmentProfessionalId));
    }

    @PreAuthorize("hasRole('PROFESSIONAL')")
    @DeleteMapping("/{annotationId}")
    public ResponseEntity<?> inactiveAnnotation(
            @PathVariable("annotationId") final UUID annotationId) {
        annotationService.inactiveAnnotation(annotationId);
        return ResponseEntity.ok().build();
    }
}

