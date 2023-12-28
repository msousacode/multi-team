package com.multiteam.modules.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.multiteam.core.exception.ProfessionalException;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.core.utils.AuthenticationUtil;
import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import com.multiteam.modules.annotation.mapper.AnnotationMapper;
import com.multiteam.modules.professional.ProfessionalService;
import com.multiteam.modules.treatment.TreatmentService;
import com.multiteam.modules.treatment.dto.TreatmentAnnotationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnnotationService {

    private final Logger logger = LogManager.getLogger(Annotation.class);

    private final static String ERROR_USUARIO_ISNOT_PROFESSIONAL = "O usuário %s não é um profissional. Somente profissionais podem realizar anotações no tratamento";

    private final AnnotationRepository annotationRepository;
    private final ProfessionalService professionalService;
    private final TreatmentService treatmentService;

    public AnnotationService(
            AnnotationRepository annotationRepository,
            ProfessionalService professionalService,
            TreatmentService treatmentService) {
        this.annotationRepository = annotationRepository;
        this.professionalService = professionalService;
        this.treatmentService = treatmentService;
    }

    @Transactional
    public void syncAnnotations(List<AnnotationDTO> annotationDTO) {
        var principal = AuthenticationUtil.getPrincipalAuthenticaded();
        var professional = professionalService.getProfessionalByUserId(UUID.fromString(principal.toString()));

        if (professional.isEmpty()) {
            logger.error(String.format(ERROR_USUARIO_ISNOT_PROFESSIONAL, principal));
            throw new ProfessionalException(String.format(ERROR_USUARIO_ISNOT_PROFESSIONAL, principal));
        }

        var annotations = annotationDTO.stream().map(AnnotationMapper.MAPPER::toEntity).toList();

        var treatmentUUIDs = annotationDTO.stream().map(AnnotationDTO::treatmentId).collect(Collectors.toSet());

        treatmentUUIDs.forEach(uuid -> {
            annotations.forEach(annotation -> {
                annotation.setTreatment(treatmentService.findTreatmentById(uuid).orElseThrow(() -> new ResourceNotFoundException("Tratamento não econtrado")));
                annotation.setActive(true);
            });
        });

        annotationRepository.saveAll(annotations);
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
