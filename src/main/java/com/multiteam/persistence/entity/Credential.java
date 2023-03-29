package com.multiteam.persistence.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "credentials")
public class Credential implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "credential_id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public Credential() {
    }

    public Credential(String username, String password) {
        if (username.isEmpty())
            throw new IllegalArgumentException("username not be empty");

        Assert.notNull(username, "username not be null");
        Assert.notNull(password, "username not be null");

        this.username = username;
        this.password = password;
    }

    public Credential(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
