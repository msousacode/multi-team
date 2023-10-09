package com.multiteam.modules.annotation;

import com.multiteam.core.models.Auditable;
import com.multiteam.modules.annotation.dto.AnnotationDetailDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "annotations")
public class Annotation extends Auditable {

  @Id
  @GeneratedValue
  @Column(name = "annotation_id")
  private UUID id;

  @Column(name = "annotation")
  private String annotation;

  @Column(name = "active")
  private Boolean active;

  @Column(name = "observation")
  private String observation;

  @Column(name = "date_initial")
  private LocalDateTime dateInitial;

  @Column(name = "date_end")
  private LocalDateTime dateEnd;

  public Annotation() {
  }

  public Annotation(UUID id, String annotation, Boolean active, String observation, LocalDateTime dateInitial,
      LocalDateTime dateEnd) {
    this.id = id;
    this.annotation = annotation;
    this.active = active;
  }

  public static Annotation toAnnoation(AnnotationDetailDTO annotationDTO) {
    Annotation annotation = new Annotation();
    annotation.setAnnotation(annotationDTO.annotation());
    annotation.setObservation(annotationDTO.observation());
    annotation.setDateInitial(LocalDateTime.of(annotationDTO.dateStart(), annotationDTO.hourStart()));
    annotation.setDateEnd(LocalDateTime.of(annotationDTO.dateEnd(), annotationDTO.hourEnd()));
    annotation.setActive(Boolean.TRUE);
    return annotation;
  }
}
