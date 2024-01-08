package com.multiteam.modules.annotation;

import com.google.common.collect.FluentIterable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, UUID> {

  @Modifying
  @Query("UPDATE Annotation a SET a.active = false WHERE a.id = :annotationId")
  void inactiveAnnotation(UUID annotationId);

  @Modifying
  @Query("DELETE FROM Annotation a WHERE a.annotationMobileId = :annotationMobileId AND a.treatment.id = :treatmentId AND a.createdBy = :principal")
  void deleteAnnotation(Integer annotationMobileId, UUID treatmentId, String principal);

  @Query("select a from Annotation a WHERE a.annotationMobileId = :annotationMobileId AND a.treatment.id = :treatmentId AND a.createdBy = :principal")
  Optional<Annotation> findAnnotationByTreatment(Integer annotationMobileId, UUID treatmentId, String principal);

  List<Annotation> findAllByTreatment_Id(UUID treatmentId);
}
