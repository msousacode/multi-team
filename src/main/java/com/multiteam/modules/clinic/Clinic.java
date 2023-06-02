package com.multiteam.modules.clinic;

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

    @Column(name = "site")
    private String site;

    @Column(name = "insc_estadual")
    private String inscEstadual;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "number")
    private String number;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

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
        this.site = builder.site;
        this.inscEstadual = builder.inscEstadual;
        this.zipCode = builder.zipCode;
        this.address = builder.address;
        this.number = builder.number;
        this.district = builder.district;
        this.city = builder.city;
        this.state = builder.state;
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

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getInscEstadual() {
        return inscEstadual;
    }

    public void setInscEstadual(String inscEstadual) {
        this.inscEstadual = inscEstadual;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

        private String site;
        private String inscEstadual;
        private String zipCode;
        private String address;
        private String number;
        private String district;
        private String city;
        private String state;

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

        public Builder site(final String site) {
            this.site = site;
            return this;
        }

        public Builder inscEstadual(final String inscEstadual) {
            this.inscEstadual = inscEstadual;
            return this;
        }

        public Builder zipCode(final String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder address(final String address) {
            this.address = address;
            return this;
        }

        public Builder number(final String number) {
            this.number = number;
            return this;
        }

        public Builder district(final String district) {
            this.district = district;
            return this;
        }

        public Builder city(final String city) {
            this.city = city;
            return this;
        }

        public Builder state(final String state) {
            this.state = state;
            return this;
        }

        public Clinic build() {
            return new Clinic(this);
        }
    }
}
