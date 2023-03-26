package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SituationType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "treatment_professional")
public record TreatmentProfessional(

        @Id
        @GeneratedValue
        @Column(name = "treatment_professional_id")
        UUID id,

        @ManyToOne
        @JoinColumn(name = "treatment_id")
        Treatment treatment,

        @ManyToOne
        @JoinColumn(name = "professional_id")
        Professional professional,

        @Column(name = "annotation")
        String annotation,

        @Column(name = "situation_type")
        @Enumerated(EnumType.STRING)
        SituationType situationType
) {
}
