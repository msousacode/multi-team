package com.multiteam.persistence.entity;

import com.multiteam.enums.SexEnum;
import org.springframework.util.Assert;

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

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private SexEnum sex;

    @Column(name = "date_birth")
    private LocalDate dateBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "active")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Patient() {
    }

    private Patient(Builder builder) {
        this.id = builder.id;
        this.ownerId = builder.ownerId;
        this.name = builder.name;
        this.sex = builder.sex;
        this.age = builder.age;
        this.active = builder.active;
        this.dateBirth = builder.dateBirth;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SexEnum getSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public static class Builder {

        //mandatory
        private UUID id;
        private final UUID ownerId;
        private final String name;
        private final SexEnum sex;
        private final Integer age;
        private boolean active;
        private final LocalDate dateBirth;

        public Builder(
                final UUID ownerId,
                final String name,
                final SexEnum sex,
                final Integer age,
                final LocalDate dateBirth) {

            Assert.notNull(dateBirth, "value dateBirth cannot be null");
            Assert.notNull(ownerId, "value ownerId cannot be null");
            Assert.isTrue(!name.isEmpty(), "patient name cannot be empty");
            Assert.notNull(sex, "patient sex cannot be null");
            Assert.notNull(age, "patient age cannot be null");

            this.ownerId = ownerId;
            this.name = name;
            this.sex = sex;
            this.age = age;
            this.dateBirth = dateBirth;
        }

        public Builder id(final UUID id) {
            this.id = id;
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

    @Override
    public String toString() {
        return "Patient{" +
               "id=" + id +
               ", ownerId=" + ownerId +
               ", name='" + name + '\'' +
               '}';
    }
}