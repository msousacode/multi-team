package com.multiteam.persistence.entity;

import com.multiteam.persistence.types.RoleType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    public UUID getId() {
        return id;
    }

    public RoleType getRole() {
        return role;
    }
}
