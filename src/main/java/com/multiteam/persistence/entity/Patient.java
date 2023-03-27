package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SexType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patients")
public class Patient {

        @Id
        @GeneratedValue
        @Column(name = "patient_id")
        private UUID id;

        @Column(name = "name")
        private String name;

        @Column(name = "middle_name")
        private String middleName;

        @Column(name = "sex")
        @Enumerated(EnumType.STRING)
        private SexType sex;

        @Column(name = "age")
        private Integer age;

        @Column(name = "months")
        private Integer months;

        @Column(name = "internal_observation")
        private LocalDate internalObservation;

        @Column(name = "external_observation")
        LocalDate externalObservation;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "clinic_id")
        private Clinic clinic;

        public Patient() {
        }
}