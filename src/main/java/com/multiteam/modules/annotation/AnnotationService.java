package com.multiteam.modules.annotation;

import com.multiteam.core.exception.ProfessionalException;
import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private ModelMapper modelMapper;

  private final AnnotationRepository annotationRepository;
  private final ProfessionalService professionalService;

  public AnnotationService(
      AnnotationRepository annotationRepository,
      ProfessionalService professionalService) {
    this.annotationRepository = annotationRepository;
    this.professionalService = professionalService;
  }

  @Transactional
  public void createAnnotation(final AnnotationDTO annotationDTO, UUID treatmentId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    var principal = authentication.getPrincipal();
    var professional = professionalService.getProfessionalByUserId(
        UUID.fromString(principal.toString()));

    if (professional.isEmpty()) {
      logger.error("Only professionals can take notes on the treatment");
      throw new ProfessionalException(
          "Apenas profissionais podem realizar anotações no tratamento");
    }
/*
    var treatmentprofessional = treatementprofessionalrepository.findbytreatment_idandprofessional_id(
        treatmentid, professional.get().getid());

    if (treatmentprofessional.isempty()) {
      logger.error("treatment not found or professional not added in treatment");
      throw new treatmentexception("treatment not found or professional not added in treatment");
    }

    var annotations = annotationdto.annotations().stream()
        .map(e -> annotation.toannoation(e, treatmentprofessional.get())).collect(
            collectors.tolist());

    annotationrepository.deleteallbytreatmentprofessional_id(treatmentprofessional.get().getid());

    annotationrepository.saveall(annotations);
    */
  }

  @Transactional
  public Boolean updateAnnotation(final AnnotationDTO annotationDTO) {

    /*var annotationResult = annotationRepository.findById(annotationDTO.treatmentId());

    if (annotationResult.isEmpty()) {
      logger.warn("annotation not found treatment_professional_id: {}",
          annotationDTO.treatmentId());
      return Boolean.FALSE;
    }

    //annotationResult.get().setAnnotation(annotationDTO.annotation());
    annotationRepository.save(annotationResult.get());
*/
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
    return null;//annotationRepository.findById(treatmentProfessionalId)
    //.map(AnnotationDTO::new).orElse(null);
  }
}
