package com.multiteam.modules.annotation;

import com.multiteam.core.exception.ProfessionalException;
import com.multiteam.core.exception.TreatmentException;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.treatment.TreatementProfessionalRepository;
import com.multiteam.modules.treatment.TreatmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class AnnotationService {

    private final Logger logger = LogManager.getLogger(Annotation.class);

    private final AnnotationRepository annotationRepository;
    private final TreatmentService treatmentService;
    private final ProfessionalService professionalService;
    private final TreatementProfessionalRepository treatementProfessionalRepository;

    public AnnotationService(
            AnnotationRepository annotationRepository,
            TreatmentService treatmentService,
            ProfessionalService professionalService,
            TreatementProfessionalRepository treatementProfessionalRepository) {
        this.annotationRepository = annotationRepository;
        this.treatmentService = treatmentService;
        this.professionalService = professionalService;
        this.treatementProfessionalRepository = treatementProfessionalRepository;
    }

    @Transactional
    public void createAnnotation(final AnnotationDTO annotationDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var principal = authentication.getPrincipal();
        var professional = professionalService.getProfessionalByUserId(UUID.fromString(principal.toString()));

        if (professional.isEmpty()) {
            logger.error("Only professionals can take notes on the treatment");
            throw new ProfessionalException("professional not found. Only professionals can take notes on the treatment");
        }

        var treatmentProfessional = treatementProfessionalRepository.findByTreatment_IdAndProfessional_Id(annotationDTO.treatmentId(), professional.get().getId());

        if (treatmentProfessional.isEmpty()) {
            logger.error("Treatment not found or professional not added in treatment");
            throw new TreatmentException("Treatment not found or professional not added in treatment");
        }

        Annotation annotation = new Annotation();
        annotation.setAnnotation(annotationDTO.annotation());
        annotation.setTreatmentProfessional(treatmentProfessional.get());
        annotation.setActive(Boolean.TRUE);

        annotationRepository.save(annotation);
    }

    public void getAnnotation(final UUID annotationId) {

    }

    public void inactiveAnnotation(final UUID annotationId) {

    }

    public List<Object> getAllAnnotations(UUID patientId) {
        return List.of();
    }
}
