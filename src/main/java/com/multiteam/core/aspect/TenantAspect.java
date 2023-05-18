package com.multiteam.core.aspect;

import com.multiteam.core.annotation.DisableTenantFilter;
import com.multiteam.core.context.TenantContext;
import com.multiteam.core.utils.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class TenantAspect {

    @Autowired
    private TenantContext tenantContext;

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* com.multiteam.core.repository.TenantableRepository+.find*(..))")
    public void beforeFindOfTenantableRepository(JoinPoint joinPoint) {
        entityManager.unwrap(Session.class).disableFilter(Constants.TENANT_FILTER_NAME);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        if (AnnotationUtils.getAnnotation(methodSignature.getMethod(), DisableTenantFilter.class) == null) {
            entityManager
                    .unwrap(Session.class)
                    .enableFilter(Constants.TENANT_FILTER_NAME)
                    .setParameter(Constants.TENANT_PARAMETER_NAME, tenantContext.getTenantId());
        }
    }
}
