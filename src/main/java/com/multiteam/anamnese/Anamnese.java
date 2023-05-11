package com.multiteam.anamnese;

import com.multiteam.core.enums.AnamneseEnum;
import com.multiteam.core.models.Tenantable;
import com.multiteam.patient.Patient;
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

@Entity
@Table(name = "professionals")
public class Anamnese extends Tenantable {

    @Id
    @GeneratedValue
    @Column(name = "anamnese_id")
    private UUID id;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "conclusion")
    private String conclusion;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AnamneseEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;

    @Column(name = "active")
    private boolean active;

    private Anamnese(Builder anamnese) {
        this.id = anamnese.id;
        this.annotation = anamnese.annotation;
        this.conclusion = anamnese.conclusion;
        this.status = anamnese.status;
        this.patient = anamnese.patient;
    }

    public UUID getId() {
        return id;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getConclusion() {
        return conclusion;
    }

    public AnamneseEnum getStatus() {
        return status;
    }

    public Patient getPatient() {
        return patient;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Anamnese{" +
               "id=" + id +
               '}';
    }

    public static class Builder {
        //mandatory
        private UUID id;
        private final AnamneseEnum status;
        private final Patient patient;

        //optional
        private String annotation;
        private String conclusion;
        private boolean active;

        public Builder(UUID id, final AnamneseEnum status, final Patient patient) {

            Assert.notNull(status, "AnamneseEnum should not be null");
            Assert.notNull(patient, "patient needs to be associated with anamnese");

            this.id = id;
            this.status = status;
            this.patient = patient;
        }

        public Builder annotation(final String annotation) {
            this.annotation = annotation;
            return this;
        }

        public Builder conclusion(final String conclusion) {
            this.conclusion = conclusion;
            return this;
        }

        public Builder active(final boolean active) {
            this.active = active;
            return this;
        }

        public Anamnese build() {
            return new Anamnese(this);
        }
    }
}
