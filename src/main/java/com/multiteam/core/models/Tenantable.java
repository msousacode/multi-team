package com.multiteam.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.multiteam.core.listeners.TenantEntityListener;
import com.multiteam.core.utils.Constants;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@FilterDef(name = Constants.TENANT_FILTER_NAME,
        parameters = @ParamDef(name = Constants.TENANT_PARAMETER_NAME, type = Constants.TENANT_PARAMETER_TYPE),
        defaultCondition = Constants.TENANT_COLUMN_NAME + " = :" + Constants.TENANT_PARAMETER_NAME)
@Filter(name = Constants.TENANT_FILTER_NAME)
@EntityListeners(TenantEntityListener.class)
public class Tenantable extends Auditable {

    @JsonIgnore
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
