package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SituationType;
import com.multiteam.persistence.types.TreatmentType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "treatments")
public record Treatment(

        @Id
        @GeneratedValue
        @Column(name = "treatment_id")
        UUID id,

        @Column(name = "description")
        String description,

        @Column(name = "treatment_type")
        @Enumerated(EnumType.STRING)
        TreatmentType treatmentType,

        @Column(name = "situation")
        @Enumerated(EnumType.STRING)
        SituationType situation,

        @Column(name = "initial_date")
        LocalDate initialDate,

        @Column(name = "final_date")
        LocalDate finalDate,

        @Column(name = "active")
        boolean active,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
        Patient patient,

        @ManyToMany
                @JoinTable(name = "treatments_guests",
                joinColumns = @JoinColumn(name = "treatment_id"),
                inverseJoinColumns = @JoinColumn(name = "guest_id"))
        Set<Guest> guests,

        @OneToMany(mappedBy = "treatment")
        Set<TreatmentProfessional> professionals
) {
}

