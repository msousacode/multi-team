package com.multiteam.modules.annotation;

import com.multiteam.core.models.Auditable;
import com.multiteam.modules.treatment.Treatment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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
  private LocalDate dateInitial;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id")
  private Treatment treatment;
}
