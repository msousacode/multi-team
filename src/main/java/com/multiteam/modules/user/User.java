package com.multiteam.modules.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.multiteam.core.enums.AuthProviderEnum;
import com.multiteam.core.enums.UserEnum;
import com.multiteam.modules.role.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
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
    private Boolean emailVerified;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProviderEnum provider;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Transient
    private String provisionalPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserEnum userType;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public User() {
    }

    public User(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.name = builder.name;
        this.email = builder.email;
        this.emailVerified = builder.emailVerified;
        this.imageUrl = builder.imageUrl;
        this.provider = builder.provider;
        this.active = builder.active;
        this.roles = builder.roles;
        this.password = builder.password;
        this.userType = builder.userType;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", tenantId='" + tenantId + '\'' +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", userType='" + userType + '\'' +
               '}';
    }

    public static class Builder {
        //mandatory
        private UUID id;
        private UUID tenantId;
        private final String name;
        private final String email;
        private final Boolean active;

        //optional
        private String password;
        private String imageUrl;
        private Boolean emailVerified;
        private AuthProviderEnum provider;
        private List<Role> roles;
        private UserEnum userType;

        public Builder(
                final UUID id,
                final UUID tenantId,
                final String name,
                final String email,
                final Boolean active) {

            this.id = id;
            this.tenantId = tenantId;
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

        public Builder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder userType(UserEnum userType) {
            this.userType = userType;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
