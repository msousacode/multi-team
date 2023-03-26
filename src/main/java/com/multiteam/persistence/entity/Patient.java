package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SexType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patients")
public record Patient(

        @Id
        @GeneratedValue
        @Column(name = "patient_id")
        UUID id,

        @Column(name = "name")
        String name,

        @Column(name = "middle_name")
        String middleName,

        @Column(name = "sex")
        @Enumerated(EnumType.STRING)
        SexType sex,

        @Column(name = "age")
        Integer age,

        @Column(name = "months")
        Integer months,

        @Column(name = "internal_observation")
        LocalDate internalObservation,

        @Column(name = "external_observation")
        LocalDate externalObservation,

        @OneToOne
        Credential credential
) {
}


