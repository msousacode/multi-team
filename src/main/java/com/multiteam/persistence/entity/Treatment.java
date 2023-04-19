package com.multiteam.persistence.entity;

import com.multiteam.enums.SituationEnum;
import com.multiteam.enums.TreatmentEnum;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "treatments")
public class Treatment {

    @Id
    @GeneratedValue
    @Column(name = "treatment_id")
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "treatment_type")
    @Enumerated(EnumType.STRING)
    private TreatmentEnum treatmentType;

    @Column(name = "situation")
    @Enumerated(EnumType.STRING)
    private SituationEnum situation;

    @Column(name = "initial_date")
    private LocalDate initialDate;

    @Column(name = "final_date")
    private LocalDate finalDate;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;

    @ManyToMany
    @JoinTable(name = "treatments_guests",
            joinColumns = @JoinColumn(name = "treatment_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id"))
    private Set<Guest> guests;

    @OneToMany(mappedBy = "treatment")
    private Set<TreatmentProfessional> treatmentProfessionals;

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TreatmentEnum getTreatmentType() {
        return treatmentType;
    }

    public SituationEnum getSituation() {
        return situation;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public boolean isActive() {
        return active;
    }

    public Patient getPatient() {
        return patient;
    }

    public Set<Guest> getGuests() {
        return guests;
    }

    public Set<TreatmentProfessional> getTreatmentProfessionals() {
        return treatmentProfessionals;
    }

    public void addGuestsInTreatment(Guest guest) {
        this.guests.add(guest);
    }

    public Treatment() {
    }

    public Treatment(UUID id, TreatmentEnum treatmentType, SituationEnum situation, Patient patient) {
        this.id = id;
        this.treatmentType = treatmentType;
        this.situation = situation;
        this.patient = patient;
    }

    private Treatment(Builder builder) {
        this.id = builder.id;
        this.treatmentType = builder.treatmentType;
        this.situation = builder.situation;
        this.initialDate = builder.initialDate;
        this.patient = builder.patient;
        this.description = builder.description;
        this.finalDate = builder.finalDate;
        this.active = builder.active;
        this.guests = builder.guests;
    }

    public static class Builder {

        //mandatory
        private UUID id;
        private final TreatmentEnum treatmentType;
        private final SituationEnum situation;
        private final LocalDate initialDate;
        private final Patient patient;

        //optional
        private String description;
        private LocalDate finalDate;
        private boolean active;
        private Set<Guest> guests;

        public Builder(
                UUID id,
                final TreatmentEnum treatmentType,
                final SituationEnum situation,
                final LocalDate initialDate,
                final Patient patient) {

            Assert.notNull(treatmentType, "treatmentType should not be null");
            Assert.notNull(situation, "situation not should be null");
            Assert.notNull(initialDate, "initialDate should not be null");
            Assert.isTrue(validateDateInitial(initialDate), "initial date must not be less than the current date");
            Assert.notNull(patient, "patient needs to be associated with treatment");

            this.id = id;
            this.treatmentType = treatmentType;
            this.situation = situation;
            this.initialDate = initialDate;
            this.patient = patient;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder finalDate(LocalDate finalDate) {
            this.finalDate = finalDate;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder description(Set<Guest> guests) {
            this.guests = guests;
            return this;
        }

        public Treatment build() {
            return new Treatment(this);
        }

        private static boolean validateDateInitial(LocalDate initialDate) {
            return !LocalDate.now().isAfter(initialDate);
        }
    }
}

