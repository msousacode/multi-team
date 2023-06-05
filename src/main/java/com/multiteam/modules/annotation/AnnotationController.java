package com.multiteam.modules.annotation;

import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/search")
    public ResponseEntity<List<TreatmentAnnotationDTO>> getAllAnnotations(@RequestBody final AnnototionSearch search) {
        return ResponseEntity.ok(annotationService.getAllAnnotations(search));
    }
/*
    @GetMapping("/annotations/{treatmentProfessionalId}")
    public ResponseEntity<Void> getAnnotationsById(@PathVariable("treatmentProfessionalId") final UUID treatmentProfessionalId) {
        return ResponseEntity.ok(annotationService.getAnnotation(treatmentProfessionalId));
    }*/
}
