package com.multiteam.guest;

import com.multiteam.core.enums.RelationshipEnum;
import com.multiteam.user.User;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue
    @Column(name = "guest_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "relationship")
    @Enumerated(EnumType.STRING)
    private RelationshipEnum relationship;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Guest() {}

    public Guest(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.middleName = builder.middleName;
        this.relationship = builder.relationship;
        this.cellPhone = builder.cellPhone;
        this.email = builder.email;
        this.active = builder.active;
        this.user = builder.user;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public RelationshipEnum getRelationship() {
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

    public User getUser() {
        return user;
    }

    public static class Builder {
        //mandatory
        private UUID id;
        private final String name;
        private final String middleName;
        private final RelationshipEnum relationship;
        private final String cellPhone;
        private final String email;
        private final boolean active;
        private final User user;

        public Builder(UUID id,
                       final String name,
                       final String middleName,
                       final RelationshipEnum relationship,
                       final String cellPhone,
                       final String email,
                       final boolean active,
                       final User user) {

            Assert.isTrue(!name.isEmpty(), "guest name should not be empty");
            Assert.isTrue(!middleName.isEmpty(), "guest middle name should not be empty");
            Assert.isTrue(!cellPhone.isEmpty(), "guest cell phone should not be empty");
            Assert.isTrue(!email.isEmpty(), "guest email should not be empty");

            Assert.notNull(name, "guest name should not be null");
            Assert.notNull(middleName,"guest middle name should not be null");
            Assert.notNull(cellPhone, "guest cell phone should not be null");
            Assert.notNull(user, "user should not be null");

            this.id = id;
            this.name = name;
            this.middleName = middleName;
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
