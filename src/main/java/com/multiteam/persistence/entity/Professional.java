package com.multiteam.persistence.entity;

import com.multiteam.enums.SpecialtyEnum;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.List;
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

    @Column(name = "specialty")
    @Enumerated(EnumType.STRING)
    private SpecialtyEnum specialty;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "professional")
    private Set<TreatmentProfessional> professionals;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Override
    public String toString() {
        return "Professional{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", specialty=" + specialty +
               '}';
    }

    @ManyToMany
    @JoinTable(
            name = "professionals_clinics",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id"))
    private List<Clinic> clinics;

    public Professional() {}

    private Professional(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.specialty = builder.specialty;
        this.cellPhone = builder.cellPhone;
        this.email = builder.email;
        this.active = builder.active;
        this.clinics = builder.clinics;
        this.user = builder.user;
        this.ownerId = builder.ownerId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SpecialtyEnum getSpecialty() {
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

    public List<Clinic> getClinics() {
        return clinics;
    }

    public Set<TreatmentProfessional> getProfessionals() {
        return professionals;
    }

    public User getUser() {
        return user;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public static class Builder {

        //mandatory
        private UUID id;
        private final String name;
        private final SpecialtyEnum specialty;
        private final String cellPhone;
        private final String email;
        private final boolean active;
        private final List<Clinic> clinics;
        private final User user;
        private final UUID ownerId;

        //optional
        private Set<TreatmentProfessional> professionals;

        public Builder(
                UUID id,
                final String name,
                final SpecialtyEnum specialty,
                final String cellPhone,
                final String email,
                final boolean active,
                final List<Clinic> clinics,
                final User user,
                final UUID ownerId) {

            Assert.notNull(name, "professional name not be null");
            Assert.notNull(specialty, "professional specialty not be null");
            Assert.notNull(cellPhone, "professional cellphone not be null");
            Assert.notNull(email, "professional email not be null");
            Assert.notNull(user, "professional needs to be associated with the user");
            Assert.notNull(ownerId, "professional needs to be associated with the owner");

            Assert.isTrue(!clinics.isEmpty(), "clinic list cannot be empty");
            Assert.isTrue(!name.isEmpty(), "professional name not be empty");
            Assert.isTrue(!cellPhone.isEmpty(), "professional cellphone not be empty");
            Assert.isTrue(!email.isEmpty(), "professional email not be empty");

            this.id = id;
            this.name = name;
            this.specialty = specialty;
            this.cellPhone = cellPhone;
            this.email = email;
            this.active = active;
            this.clinics = clinics;
            this.user = user;
            this.ownerId = ownerId;
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


