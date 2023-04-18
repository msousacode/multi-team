package com.multiteam.persistence.entity;

import com.multiteam.persistence.enums.RoleEnum;

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
    private RoleEnum role;

    public Role() {}

    public Role(UUID id, RoleEnum role){
        this.id = id;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public RoleEnum getRole() {
        return role;
    }
}
