package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SpecialtyType;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "professionals")
public class Professional {

    @Id
    @GeneratedValue
    @Column(name = "professional_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "specialty")
    @Enumerated(EnumType.STRING)
    private SpecialtyType specialty;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @OneToMany(mappedBy = "professional")
    private Set<TreatmentProfessional> professionals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", referencedColumnName = "clinic_id")
    private Clinic clinic;

    public Professional() {}

    private Professional(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.middleName = builder.middleName;
        this.specialty = builder.specialty;
        this.cellPhone = builder.cellPhone;
        this.email = builder.email;
        this.credential = builder.credential;
        this.active = builder.active;
        this.clinic = builder.clinic;
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

    public SpecialtyType getSpecialty() {
        return specialty;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    public Credential getCredential() {
        return credential;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public Set<TreatmentProfessional> getProfessionals() {
        return professionals;
    }

    public static class Builder {

        //mandatory
        private UUID id;
        private final String name;
        private final String middleName;
        private final SpecialtyType specialty;
        private final String cellPhone;
        private final String email;
        private final boolean active;
        private Credential credential;
        private final Clinic clinic;

        //optional
        private Set<TreatmentProfessional> professionals;

        public Builder(
                UUID id,
                final String name,
                final String middleName,
                final SpecialtyType specialty,
                final String cellPhone,
                final String email,
                final boolean active,
                final Clinic clinic) {

            Assert.notNull(name, "professional name not be null");
            Assert.notNull(middleName, "professional middle name not be null");
            Assert.notNull(specialty, "professional specialty not be null");
            Assert.notNull(cellPhone, "professional cellphone not be null");
            Assert.notNull(email, "professional email not be null");
            Assert.notNull(clinic, "professional needs to be associated with the clinic");
            Assert.isTrue(!name.isEmpty(), "professional name not be empty");
            Assert.isTrue(!middleName.isEmpty(), "professional middle name not be empty");
            Assert.isTrue(!cellPhone.isEmpty(), "professional cellphone not be empty");
            Assert.isTrue(!email.isEmpty(), "professional email not be empty");

            this.id = id;
            this.name = name;
            this.middleName = middleName;
            this.specialty = specialty;
            this.cellPhone = cellPhone;
            this.email = email;
            this.active = active;
            this.clinic = clinic;
        }

        public Builder credential(Credential credential) {
            Assert.notNull(credential, "credential not be null");
            this.credential = credential;
            return this;
        }

        public Builder professionals(Set<TreatmentProfessional> professionals) {
            this.professionals = professionals;
            return this;
        }

        public Professional build() {
            return new Professional(this);
        }
    }
}


