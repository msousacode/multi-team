package com.multiteam.modules.schedule;

import com.multiteam.core.enums.ScheduleEnum;
import com.multiteam.core.models.Tenantable;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.professional.Professional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schedules")
public class Schedule extends Tenantable {

    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "date_start")
    private LocalDateTime start;

    @Column(name = "date_end")
    private LocalDateTime end;

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    private String color;

    @Column(name = "active")
    private boolean active;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ScheduleEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    public Schedule(){}

    public Schedule(Builder builder) {
        this.title = builder.title;
        this.start = builder.start;
        this.professional = builder.professional;
        this.clinic = builder.clinic;
        this.active = builder.active;
        this.status = builder.status;
        this.id = builder.id;
        this.patient = builder.patient;
        this.end = builder.end;
        this.url = builder.url;
        this.description = builder.description;
        this.color = builder.color;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public boolean isActive() {
        return active;
    }

    public ScheduleEnum getStatus() {
        return status;
    }

    public Patient getPatient() {
        return patient;
    }

    public Professional getProfessional() {
        return professional;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public static class Builder {

        //mandatory
        private final String title;
        private final LocalDateTime start;
        private final Professional professional;
        private final Clinic clinic;
        private final boolean active;
        private final ScheduleEnum status;

        //optionals
        private UUID id;
        private Patient patient;
        private LocalDateTime end;
        private String url;
        private String description;
        private String color;

        public Builder(
                final String title,
                final LocalDateTime start,
                final Professional professional,
                final Clinic clinic,
                final boolean active,
                final ScheduleEnum status) {

            //Asserts
            Assert.isTrue(StringUtils.hasText(title), "title value cannot be null or empty");
            Assert.notNull(start, "start schedule value cannot be null or empty");
            Assert.notNull(professional, "professional value cannot be null or empty");
            Assert.notNull(clinic, "clinic value cannot be null or empty");
            Assert.notNull(status, "status value cannot be null or empty");

            this.title = title;
            this.start = start;
            this.professional = professional;
            this.clinic = clinic;
            this.active = active;
            this.status = status;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder patient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public Builder end(LocalDateTime end) {
            this.end = end;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Schedule build() {
            return new Schedule(this);
        }
    }
}
