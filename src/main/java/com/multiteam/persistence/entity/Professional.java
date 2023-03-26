package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.SexType;
import com.multiteam.persistence.types.SpecialtyType;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "professionals")
public record Professional(

        @Id
        @GeneratedValue
        @Column(name = "professional_id")
        UUID id,

        @Column(name = "name")
        String name,

        @Column(name = "middle_name")
        String middleName,

        @Column(name = "specialty")
        @Enumerated(EnumType.STRING)
        SexType specialty,

        @Column(name = "cell_phone")
        String cellPhone,

        @Column(name = "email")
        String email,

        @Column(name = "active")
        boolean active,

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "credential_id")
        Credential credential,

        @OneToMany(mappedBy = "professional")
        Set<TreatmentProfessional> professionals
) {
}


