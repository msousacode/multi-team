package com.multiteam.modules.annotation;

import com.multiteam.core.exception.ProfessionalException;
import com.multiteam.core.exception.TreatmentException;
import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.treatment.TreatementProfessionalRepository;
import com.multiteam.modules.treatment.TreatmentService;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
import com.multiteam.modules.treatment.dto.TreatmentProfessionalAnnotationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
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

    @Transactional
    public Boolean updateAnnotation(final AnnotationDTO annotationDTO) {

        var annotationResult = annotationRepository.findById(annotationDTO.treatmentId());

        if (annotationResult.isEmpty()) {
            logger.warn("annotation not found treatment_professional_id: {}", annotationDTO.treatmentId());
            return Boolean.FALSE;
        }

        annotationResult.get().setAnnotation(annotationDTO.annotation());
        annotationRepository.save(annotationResult.get());

        return Boolean.TRUE;
    }

    public void inactiveAnnotation(final UUID annotationId) {

    }

    public List<TreatmentAnnotationDTO> getAllAnnotations(final AnnototionSearch search) {

        Specification<Annotation> spec = AnnotationSpecification.findAllAnnotations(search);
        var list = annotationRepository.findAll(spec);

        return list.stream().map(TreatmentAnnotationDTO::new).toList();
    }

    public AnnotationDTO getAnnotation(final UUID treatmentProfessionalId) {
        return annotationRepository.findById(treatmentProfessionalId)
                .map(AnnotationDTO::new).orElse(null);
    }
}
