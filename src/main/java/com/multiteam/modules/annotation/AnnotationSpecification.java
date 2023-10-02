package com.multiteam.modules.annotation;

import com.multiteam.core.exception.BadRequestException;
import com.multiteam.modules.annotation.dto.AnnototionSearch;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

public class AnnotationSpecification {

    public static Specification<Annotation> findAllAnnotations(AnnototionSearch search) {
/*
        return (root, query, builder) -> {
            Join<TreatmentProfessional, Annotation> annotationsJoin = root.join("treatmentProfessional", JoinType.INNER);

            if (search.professionalId() != null && search.treatmentId() != null) {
                Predicate professional = builder.equal(annotationsJoin.get("id"), search.professionalId());
                Predicate treatment = builder.equal(annotationsJoin.get("treatment").get("id"), search.treatmentId());
                Predicate activeTrue = builder.equal(root.get("active"), Boolean.TRUE);

                return builder.and(treatment, professional, activeTrue);

            } else if (search.patientId() != null) {
                return builder.equal(annotationsJoin.get("treatment").get("patient").get("id"), search.patientId());
            } else {
                throw new BadRequestException("not found parameters to this consult");
            }
        };
    }*/
        return null;
    }
}