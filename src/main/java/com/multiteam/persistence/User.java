package com.multiteam.persistence;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id", nullable = false))
public class User implements UserDetails {

    public User() {
    }

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID id;

    @Email(message = "invalid email format")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "invalid name format")
    @Column(name = "name")
    private String name;

    @Column(name = "password")
    @NotEmpty(message = "invalid password format")
    private String password;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return null;
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
