package com.multiteam.core.contexts;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TenantContext {

    public UUID getTenantId() {
        //return (SecurityContextHolder.getContext().getAuthentication()).getTenantId();
        return UUID.randomUUID();
    }
}
