package com.multiteam.modules.annotation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
/*
    @GetMapping("/patient/{patientId}/annotations")
    public ResponseEntity<List<TreatmentAnnotationDTO>> getAnnotations(@PathVariable("patientId") final UUID patientId) {
        return ResponseEntity.ok(annotationService.getAllAnnotations(patientId));
    }

    @GetMapping("/annotations/{treatmentProfessionalId}")
    public ResponseEntity<Void> getAnnotationsById(@PathVariable("treatmentProfessionalId") final UUID treatmentProfessionalId) {
        return ResponseEntity.ok(annotationService.getAnnotation(treatmentProfessionalId));
    }*/
}
