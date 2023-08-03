package com.multiteam.modules.annotation;

import com.multiteam.core.models.Auditable;
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

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getAnnotation() {
    return annotation;
  }

  public void setAnnotation(String annotation) {
    this.annotation = annotation;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public TreatmentProfessional getTreatmentProfessional() {
    return treatmentProfessional;
  }

  public void setTreatmentProfessional(TreatmentProfessional treatmentProfessional) {
    this.treatmentProfessional = treatmentProfessional;
  }

  public String getObservation() {
    return observation;
  }

  public LocalDateTime getDateInitial() {
    return dateInitial;
  }

  public LocalDateTime getDateEnd() {
    return dateEnd;
  }
}
