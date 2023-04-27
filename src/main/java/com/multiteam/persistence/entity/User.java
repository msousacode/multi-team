package com.multiteam.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.multiteam.enums.AuthProviderEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProviderEnum provider;

    @Column(name = "active")
    private Boolean active;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.emailVerified = builder.emailVerified;
        this.imageUrl = builder.imageUrl;
        this.provider = builder.provider;
        this.active = builder.active;
        this.roles = builder.roles;
        this.password = builder.password;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public AuthProviderEnum getProvider() {
        return provider;
    }

    public Boolean getActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static User createUserActive(String email, String password) {
        return new User.Builder(UUID.randomUUID(), "Basic User", email, true).password(password).build();
    }

    public static class Builder {
        //mandatory
        private UUID id;
        private final String name;
        private final String email;
        private final Boolean active;

        //optional
        private String password;
        private String imageUrl;
        private Boolean emailVerified;
        private AuthProviderEnum provider;
        private Set<Role> roles;

        public Builder(
                final UUID id,
                final String name,
                final String email,
                final Boolean active) {

            this.id = id;
            this.name = name;
            this.email = email;
            this.active = active;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder emailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder provider(AuthProviderEnum provider) {
            this.provider = provider;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
