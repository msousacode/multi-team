package com.multiteam.persistence.entity;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Id
    @GeneratedValue
    @Column(name = "clinic_id")
    private UUID id;

    @Column(name = "clinic_name")
    private String clinicName;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    @Column(name = "email")
    private String email;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "observation")
    private String observation;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "removed_date")
    private LocalDateTime removedDate;

    @OneToMany(mappedBy = "clinic")
    private List<Professional> professionals;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Clinic() {}

    public Clinic(Builder builder) {
        this.id = builder.id;
        this.clinicName = builder.clinicName;
        this.cpfCnpj = builder.cpfCnpj;
        this.email = builder.email;
        this.cellPhone = builder.cellPhone;
        this.telephone = builder.telephone;
        this.observation = builder.observation;
        this.active = builder.active;
        this.createdDate = builder.createdDate;
        this.removedDate = builder.removedDate;
        this.user = builder.user;
    }

    public UUID getId() {
        return id;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getObservation() {
        return observation;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getRemovedDate() {
        return removedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public List<Professional> getProfessionals() {
        return professionals;
    }

    public User getUser() {
        return user;
    }

    public static class Builder {

        //mandatory
        private UUID id;
        private final String clinicName;
        private final String cpfCnpj;
        private final String email;
        private final String cellPhone;
        private final User user;

        //optional
        private String telephone;
        private String observation;
        private Boolean active;
        private LocalDateTime createdDate;
        private LocalDateTime removedDate;

        public Builder(final String clinicName, final String cpfCnpj, final String email, final String cellPhone, final User user) {

            Assert.notNull(user, "clinic needs to be associated with the user owner");
            Assert.notNull(clinicName, "clinic name not be null");
            Assert.notNull(cpfCnpj, "clinic cpf_cnpj not be null");
            Assert.notNull(email, "clinic email not be null");
            Assert.notNull(cellPhone, "clinic cell phone not be null");
            Assert.isTrue(!clinicName.isEmpty(), "clinic name not be empty");
            Assert.isTrue(!cpfCnpj.isEmpty(), "clinic cpf_cnpj not be empty");
            Assert.isTrue(!email.isEmpty(), "clinic email not be empty");
            Assert.isTrue(!cellPhone.isEmpty(), "clinic cell phone not be empty");

            this.clinicName = clinicName;
            this.cpfCnpj = cpfCnpj;
            this.email = email;
            this.cellPhone = cellPhone;
            this.user = user;
        }

        public Builder id(final UUID id) {
            this.id = id;
            return this;
        }

        public Builder telephone(final String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder observation(final String observation) {
            this.observation = observation;
            return this;
        }

        public Builder active(final Boolean active) {
            this.active = active;
            return this;
        }

        public Builder createdDate(final LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder removedDate(final LocalDateTime removedDate) {
            this.removedDate = removedDate;
            return this;
        }

        public Clinic build() {
            return new Clinic(this);
        }
    }
}
