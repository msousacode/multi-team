package com.multiteam.modules.annotation;

import com.multiteam.core.models.Auditable;
import com.multiteam.modules.annotation.dto.AnnotationDetailDTO;
import com.multiteam.modules.treatment.TreatmentProfessional;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "treatment_professional_id")
  private TreatmentProfessional treatmentProfessional;

  @Column(name = "observation")
  private String observation;

  @Column(name = "date_initial")
  private LocalDateTime dateInitial;

  @Column(name = "date_end")
  private LocalDateTime dateEnd;

  public Annotation() {
  }

  public Annotation(UUID id, String annotation, Boolean active,
      TreatmentProfessional treatmentProfessional, String observation, LocalDateTime dateInitial,
      LocalDateTime dateEnd) {
    this.id = id;
    this.annotation = annotation;
    this.active = active;
    this.treatmentProfessional = treatmentProfessional;
  }

  public static Annotation toAnnoation(AnnotationDetailDTO annotationDTO, TreatmentProfessional treatmentProfessional) {
    Annotation annotation = new Annotation();
    annotation.setTreatmentProfessional(treatmentProfessional);
    annotation.setAnnotation(annotationDTO.annotation());
    annotation.setObservation(annotationDTO.observation());
    annotation.setDateInitial(LocalDateTime.of(annotationDTO.dateStart(), annotationDTO.hourStart()));
    annotation.setDateEnd(LocalDateTime.of(annotationDTO.dateEnd(), annotationDTO.hourEnd()));
    annotation.setActive(Boolean.TRUE);
    return annotation;
  }
}
