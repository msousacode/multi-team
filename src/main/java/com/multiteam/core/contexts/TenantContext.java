package com.multiteam.core.contexts;

import com.multiteam.core.authentication.TenantAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TenantContext {

    public UUID getTenantId() {
        return ((TenantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getTenantId();
    }
}
