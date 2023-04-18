package com.multiteam.persistence.entity;

import com.multiteam.persistence.enums.SituationType;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "treatments_professionals")
public class TreatmentProfessional {

        @Id
        @GeneratedValue
        @Column(name = "treatment_professional_id")
        private UUID id;

        @ManyToOne
        @JoinColumn(name = "treatment_id")
        private Treatment treatment;

        @ManyToOne
        @JoinColumn(name = "professional_id")
        private Professional professional;

        @Column(name = "annotation")
        private String annotation;

        @Column(name = "situation_type")
        @Enumerated(EnumType.STRING)
        private SituationType situationType;

        public TreatmentProfessional() {}

        public TreatmentProfessional(
                UUID id,
                Treatment treatment,
                Professional professional,
                String annotation,
                SituationType situationType) {

                Assert.notNull(treatment, "treatment should not be null");
                Assert.notNull(professional, "professional should not be null");
                Assert.notNull(situationType, "situationType should not be null");

                this.id = id;
                this.treatment = treatment;
                this.professional = professional;
                this.annotation = annotation;
                this.situationType = situationType;
        }

        public UUID getId() {
                return id;
        }

        public Treatment getTreatment() {
                return treatment;
        }

        public Professional getProfessional() {
                return professional;
        }

        public String getAnnotation() {
                return annotation;
        }

        public SituationType getSituationType() {
                return situationType;
        }
}
