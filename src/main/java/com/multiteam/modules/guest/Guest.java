package com.multiteam.modules.guest;

import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue
    @Column(name = "guest_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "relationship")
    private Integer relationship;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Guest() {}

    public Guest(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.relationship = builder.relationship;
        this.cellPhone = builder.cellPhone;
        this.email = builder.email;
        this.active = builder.active;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getRelationship() {
        return relationship;
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

    public static class Builder {
        //mandatory
        private UUID id;
        private final String name;
        private final Integer relationship;
        private final String cellPhone;
        private final String email;
        private final boolean active;
        private final User user;

        public Builder(UUID id,
                       final String name,
                       final Integer relationship,
                       final String cellPhone,
                       final String email,
                       final boolean active,
                       final User user) {

            Assert.isTrue(!name.isEmpty(), "guest name should not be empty");
            Assert.isTrue(!cellPhone.isEmpty(), "guest cell phone should not be empty");
            Assert.isTrue(!email.isEmpty(), "guest email should not be empty");

            Assert.notNull(name, "guest name should not be null");
            Assert.notNull(cellPhone, "guest cell phone should not be null");
            Assert.notNull(user, "user should not be null");

            this.id = id;
            this.name = name;
            this.relationship = relationship;
            this.cellPhone = cellPhone;
            this.email = email;
            this.active = active;
            this.user = user;
        }

        public Guest build() {
            return new Guest(this);
        }
    }
}
