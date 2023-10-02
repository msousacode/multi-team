package com.multiteam.modules.professional;

import com.multiteam.core.enums.SpecialtyEnum;
import com.multiteam.core.models.Tenantable;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.treatment.TreatmentProfessional;
import com.multiteam.modules.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "professionals")
public class Professional extends Tenantable {

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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

    @ManyToMany
    @JoinTable(
            name = "professionals_folders",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "folder_id"))
    private List<Folder> folders;

    public Professional() {
        super();
    }

    private Professional(Builder builder) {
        this();
        this.id = builder.id;
        this.name = builder.name;
        this.specialty = builder.specialty;
        this.cellPhone = builder.cellPhone;
        this.email = builder.email;
        this.active = builder.active;
        this.clinics = builder.clinics;
        this.user = builder.user;
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
                final User user) {

            Assert.notNull(name, "professional name not be null");
            Assert.notNull(specialty, "professional specialty not be null");
            Assert.notNull(cellPhone, "professional cellphone not be null");
            Assert.notNull(email, "professional email not be null");
            Assert.notNull(user, "professional needs to be associated with the user");

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


