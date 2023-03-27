package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.RelationshipType;
import com.multiteam.persistence.types.SexType;

import javax.persistence.*;
import java.time.LocalDate;
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
        private RelationshipType relationship;

        @Column(name = "cell_phone")
        private String cellPhone;

        @Column(name = "email")
        private String email;

        @Column(name = "active")
        private boolean active;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "credential_id")
        Credential credential;
}


