package com.multiteam.modules.annotation;

import com.multiteam.core.enums.AnnotationSync;
import com.multiteam.core.exception.ResourceNotFoundException;
import com.multiteam.core.utils.AuthenticationUtil;
import com.multiteam.modules.annotation.dto.AnnotationDTO;
import com.multiteam.modules.annotation.mapper.AnnotationMapper;
import com.multiteam.modules.treatment.TreatmentService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnnotationService {

    private final AnnotationRepository annotationRepository;
    private final TreatmentService treatmentService;

    public AnnotationService(
            AnnotationRepository annotationRepository,
            TreatmentService treatmentService) {
        this.annotationRepository = annotationRepository;
        this.treatmentService = treatmentService;
    }

    @Transactional
    public void syncAnnotations(List<AnnotationDTO> annotationDTO) {

        var annotations = annotationDTO.stream().map(AnnotationMapper.MAPPER::toEntity).toList();

        var treatmentUUIDs = annotationDTO.stream().map(AnnotationDTO::treatmentId).collect(Collectors.toSet());

        treatmentUUIDs.forEach(uuid -> annotations.forEach(annotation -> {
            annotation.setTreatment(treatmentService.findTreatmentById(uuid).orElseThrow(() -> new ResourceNotFoundException("Tratamento não econtrado")));
            annotation.setActive(true);

            if (annotation.getSync().equals(AnnotationSync.INATIVAR.getSync())) {
                deleteAnnotation(annotation.getAnnotationMobileId(), annotation.getTreatment().getId());

            } else if (annotation.getSync().equals(AnnotationSync.ATUALIZAR.getSync())) {

                var result = findAnnotation(annotation.getAnnotationMobileId(), annotation.getTreatment().getId());
                result.setAnnotation(annotation.getAnnotation());
                result.setObservation(annotation.getObservation());
                result.setDateInitial(annotation.getDateInitial());

                annotationRepository.save(result);

            } else {
                annotationRepository.save(annotation);
            }
        }));
    }

    private Annotation findAnnotation(Integer annotationMobileId, UUID treatmentId) {
        var principal = AuthenticationUtil.getPrincipalAuthenticaded();
        return annotationRepository.findAnnotationByTreatment(annotationMobileId, treatmentId, principal + "").get();
    }

    private void deleteAnnotation(Integer annotationMobileId, UUID treatmentId) {
        var principal = AuthenticationUtil.getPrincipalAuthenticaded();
        annotationRepository.deleteAnnotation(annotationMobileId, treatmentId, principal + "");
    }

    @Transactional
    public void inactiveAnnotation(final UUID annotationId) {
        annotationRepository.inactiveAnnotation(annotationId);
    }

    public List<AnnotationDTO> findAnnotations(UUID treatmentId) {
        return annotationRepository.findAll().stream().map(AnnotationMapper.MAPPER::toDTO).toList();
    }
}
