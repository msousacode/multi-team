package com.multiteam.core.models;

import com.multiteam.modules.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class UserPrincipal implements OAuth2User, UserDetails {

    private final UUID id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private final UUID tenantId;

    public UserPrincipal(UUID id, String email, String password, Collection<? extends GrantedAuthority> authorities, UUID tenantId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.tenantId = tenantId;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole().name())));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getTenantId()
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
