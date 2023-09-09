package com.multiteam.modules.patient.repository;

import com.multiteam.core.repository.TenantableRepository;
import com.multiteam.modules.patient.model.InappropriateBehavior;
import org.springframework.stereotype.Repository;

@Repository
public interface InappropriateBehaviorRepository extends TenantableRepository<InappropriateBehavior> {
}
