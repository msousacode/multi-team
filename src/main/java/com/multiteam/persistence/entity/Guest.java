package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.RelationshipType;
import com.multiteam.persistence.types.SexType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "guests")
public record Guest(

        @Id
        @GeneratedValue
        @Column(name = "guest_id")
        UUID id,

        @Column(name = "name")
        String name,

        @Column(name = "middle_name")
        String middleName,

        @Column(name = "relationship")
        @Enumerated(EnumType.STRING)
        RelationshipType relationship,

        @Column(name = "cell_phone")
        String cellPhone,

        @Column(name = "email")
        String email,

        @Column(name = "active")
        boolean active,

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "credential_id")
        Credential credential
) {
}


