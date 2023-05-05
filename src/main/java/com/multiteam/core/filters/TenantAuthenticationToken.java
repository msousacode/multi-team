package com.multiteam.core.filters;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class TenantAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final UUID tenantId;

    public TenantAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities, UUID tenantId) {
        super(authorities);
        setAuthenticated(true);
        this.principal = principal;
        this.tenantId = tenantId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public UUID getTenantId() {
        return this.tenantId;
    }
}