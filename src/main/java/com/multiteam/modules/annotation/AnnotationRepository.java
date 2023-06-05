package com.multiteam.modules.annotation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, UUID>, JpaSpecificationExecutor<Annotation> {
}
