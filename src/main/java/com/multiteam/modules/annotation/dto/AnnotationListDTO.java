package com.multiteam.modules.annotation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.annotation.Annotation;
import com.multiteam.modules.user.UserDTO;
import com.multiteam.modules.user.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class AnnotationListDTO {

    @Autowired
    private UserRepository userRepository;

    private UUID id;

    private String createdBy;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateInitial;

    private String annotation;

    private String observation;

    public AnnotationListDTO toDTO(Annotation annotation, UserDTO user) {
        AnnotationListDTO annotationDTO = new AnnotationListDTO();
        annotationDTO.setId(annotation.getId());
        annotationDTO.setCreatedBy(user.name());
        annotationDTO.setDateInitial(annotation.getDateInitial());
        annotationDTO.setAnnotation(annotation.getAnnotation());
        annotationDTO.setObservation(annotation.getObservation());

        return annotationDTO;
    }
}
