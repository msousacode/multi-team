package com.multiteam.modules.treatment;

import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.core.models.Tenantable;
import com.multiteam.modules.guest.Guest;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.program.entity.Folder;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "treatments")
public class Treatment extends Tenantable {

  @Id
  @GeneratedValue
  @Column(name = "treatment_id")
  private UUID id;

  @Column(name = "description")
  private String description;

  @Column(name = "situation")
  @Enumerated(EnumType.STRING)
  private TreatmentEnum situation;

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
  private List<Folder> folders = new ArrayList<>();

  public void addGuestsInTreatment(Guest guest) {
    this.guests.add(guest);
  }

  public Treatment() {
  }

  public Treatment(UUID id, TreatmentEnum situation, Patient patient) {
    this.id = id;
    this.situation = situation;
    this.patient = patient;
  }

  private Treatment(Builder builder) {
    this.id = builder.id;
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
    private final TreatmentEnum situation;
    private final LocalDate initialDate;
    private final Patient patient;

    //optional
    private String description;
    private LocalDate finalDate;
    private boolean active;
    private Set<Guest> guests;

    public Builder(
        UUID id,
        final TreatmentEnum situation,
        final LocalDate initialDate,
        final Patient patient) {

      Assert.notNull(situation, "situation not should be null");
      Assert.notNull(initialDate, "initialDate should not be null");
      Assert.notNull(patient, "patient needs to be associated with treatment");

      this.id = id;
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
  }
}

