package com.multiteam.clinic;

import com.multiteam.core.models.Tenantable;
import org.springframework.util.Assert;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "clinics")
public class Clinic extends Tenantable {

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

    public Clinic() {
    }

    public Clinic(final Builder builder) {
        this.id = builder.id;
        this.clinicName = builder.clinicName;
        this.cpfCnpj = builder.cpfCnpj;
        this.email = builder.email;
        this.cellPhone = builder.cellPhone;
        this.telephone = builder.telephone;
        this.observation = builder.observation;
        this.active = builder.active;
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

    public Boolean getActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Clinic{" +
               "id=" + id +
               ", clinicName='" + clinicName + '\'' +
               ", cpfCnpj='" + cpfCnpj + '\'' +
               ", email='" + email + '\'' +
               '}';
    }

    public static class Builder {

        //mandatory
        private UUID id;
        private final String clinicName;
        private final String cpfCnpj;
        private final String email;
        private final String cellPhone;

        //optional
        private String telephone;
        private String observation;
        private Boolean active;

        public Builder(final String clinicName, final String cpfCnpj, final String email, final String cellPhone) {

            Assert.notNull(clinicName, "clinic name not be null");
            Assert.notNull(cpfCnpj, "clinic cpfCnpj not be null");
            Assert.notNull(email, "clinic email not be null");
            Assert.notNull(cellPhone, "clinic cell phone not be null");
            Assert.isTrue(!clinicName.isEmpty(), "clinic name not be empty");
            Assert.isTrue(!cpfCnpj.isEmpty(), "clinic cpfCnpj not be empty");
            Assert.isTrue(!email.isEmpty(), "clinic email not be empty");
            Assert.isTrue(!cellPhone.isEmpty(), "clinic cell phone not be empty");

            this.clinicName = clinicName;
            this.cpfCnpj = cpfCnpj;
            this.email = email;
            this.cellPhone = cellPhone;
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

        public Clinic build() {
            return new Clinic(this);
        }
    }
}
