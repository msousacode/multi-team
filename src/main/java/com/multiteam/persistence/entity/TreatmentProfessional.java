package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SituationType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "treatment_professional")
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
}
