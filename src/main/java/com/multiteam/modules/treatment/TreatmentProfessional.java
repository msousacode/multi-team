package com.multiteam.modules.treatment;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.models.Auditable;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.professional.Professional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "treatments_professionals")
public class TreatmentProfessional extends Auditable {

        @Id
        @GeneratedValue
        @Column(name = "treatment_professional_id")
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "treatment_id")
        private Treatment treatment;

        @ManyToOne
        @JoinColumn(name = "professional_id")
        private Professional professional;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "clinic_id")
        private Clinic clinic;

        @Column(name = "situation_type")
        @Enumerated(EnumType.STRING)
        private SituationEnum situationType;

        @Column(name = "annotation")
        private String annotation;

        @Column(name = "active")
        private Boolean active;

        public TreatmentProfessional(
                final UUID id,
                final Treatment treatment,
                final Professional professional,
                final Clinic clinic,
                final SituationEnum situationType) {

                Assert.notNull(treatment, "treatment should not be null");
                Assert.notNull(professional, "professional should not be null");
                Assert.notNull(professional, "clinic should not be null");
                Assert.notNull(situationType, "situationType should not be null");

                this.id = id;
                this.treatment = treatment;
                this.professional = professional;
                this.clinic = clinic;
                this.situationType = situationType;
                this.active = true;
        }
}
