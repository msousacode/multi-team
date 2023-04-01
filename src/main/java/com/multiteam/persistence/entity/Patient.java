package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SexType;
import org.springframework.util.Assert;

import javax.persistence.*;
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
    private String internalObservation;

    @Column(name = "external_observation")
    private String externalObservation;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    public Patient() {
    }

    private Patient(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.middleName = builder.middleName;
        this.sex = builder.sex;
        this.age = builder.age;
        this.clinic = builder.clinic;
        this.months = builder.months;
        this.internalObservation = builder.internalObservation;
        this.externalObservation = builder.externalObservation;
        this.active = builder.active;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public SexType getSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getMonths() {
        return months;
    }

    public String getInternalObservation() {
        return internalObservation;
    }

    public String getExternalObservation() {
        return externalObservation;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public boolean isActive() {
        return active;
    }

    public static class Builder {

        //mandatory
        private UUID id;
        private final String name;
        private final String middleName;
        private final SexType sex;
        private final Integer age;
        private final Clinic clinic;

        //optional
        private Integer months;
        private String internalObservation;
        private String externalObservation;
        private boolean active;

        public Builder(
                final String name,
                final String middleName,
                final SexType sex,
                final Integer age,
                final Clinic clinic) {

            Assert.isTrue(!name.isEmpty(), "patient name cannot be empty");
            Assert.isTrue(!middleName.isEmpty(), "patient middle name cannot be empty");
            Assert.notNull(sex, "patient sex cannot be null");
            Assert.notNull(age, "patient age cannot be null");
            Assert.notNull(clinic, "patient needs to be associated with the clinic");

            this.name = name;
            this.middleName = middleName;
            this.sex = sex;
            this.age = age;
            this.clinic = clinic;
        }

        public Builder months(Integer months) {
            this.months = months;
            return this;
        }

        public Builder internalObservation(String internalObservation) {
            this.internalObservation = internalObservation;
            return this;
        }

        public Builder externalObservation(String externalObservation) {
            this.externalObservation = externalObservation;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Patient build() {
            return new Patient(this);
        }
    }
}