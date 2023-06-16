package com.multiteam.modules.annotation;

import com.multiteam.core.exception.ProfessionalException;
import com.multiteam.core.exception.TreatmentException;
import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.treatment.TreatementProfessionalRepository;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {

  private final Logger logger = LogManager.getLogger(Annotation.class);

  private final AnnotationRepository annotationRepository;
  private final ProfessionalService professionalService;
  private final TreatementProfessionalRepository treatementProfessionalRepository;

  public AnnotationService(
      AnnotationRepository annotationRepository,
      ProfessionalService professionalService,
      TreatementProfessionalRepository treatementProfessionalRepository) {
    this.annotationRepository = annotationRepository;
    this.professionalService = professionalService;
    this.treatementProfessionalRepository = treatementProfessionalRepository;
  }

  @Transactional
  public void createAnnotation(final AnnotationDTO annotationDTO) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    var principal = authentication.getPrincipal();
    var professional = professionalService.getProfessionalByUserId(
        UUID.fromString(principal.toString()));

    if (professional.isEmpty()) {
      logger.error("Only professionals can take notes on the treatment");
      throw new ProfessionalException(
          "Apenas profissionais podem realizar anotações no tratamento");
    }

    var treatmentProfessional = treatementProfessionalRepository.findByTreatment_IdAndProfessional_Id(
        annotationDTO.treatmentId(), professional.get().getId());

    if (treatmentProfessional.isEmpty()) {
      logger.error("treatment not found or professional not added in treatment");
      throw new TreatmentException("treatment not found or professional not added in treatment");
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
      logger.warn("annotation not found treatment_professional_id: {}",
          annotationDTO.treatmentId());
      return Boolean.FALSE;
    }

    annotationResult.get().setAnnotation(annotationDTO.annotation());
    annotationRepository.save(annotationResult.get());

    return Boolean.TRUE;
  }

  @Transactional
  public void inactiveAnnotation(final UUID annotationId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    annotationRepository.inactiveAnnotation(annotationId);
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
