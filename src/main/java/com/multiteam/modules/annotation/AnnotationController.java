package com.multiteam.modules.annotation;

import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/annotations",
        produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE
)
@RestController
public class AnnotationController {

    private final AnnotationService annotationService;

    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @PostMapping
    public ResponseEntity<Void> includeAnnotation(@RequestBody final AnnotationDTO annotationDTO) {
        annotationService.createAnnotation(annotationDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> upddateAnnotation(@RequestBody final AnnotationDTO annotationDTO) {
        return annotationService.updateAnnotation(annotationDTO) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<TreatmentAnnotationDTO>> getAllAnnotations(@RequestBody final AnnototionSearch search) {
        return ResponseEntity.ok(annotationService.getAllAnnotations(search));
    }

    @GetMapping("/{treatmentProfessionalId}")
    public ResponseEntity<AnnotationDTO> getAnnotation(@PathVariable("treatmentProfessionalId") final UUID treatmentProfessionalId) {
        return ResponseEntity.ok(annotationService.getAnnotation(treatmentProfessionalId));
    }

    @DeleteMapping("/{annotationId}")
    public ResponseEntity<?> inactiveAnnotation(@PathVariable("annotationId") final UUID annotationId) {
        annotationService.inactiveAnnotation(annotationId);
        return ResponseEntity.ok().build();
    }
}
