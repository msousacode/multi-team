package com.multiteam.modules.patient.repository;

import com.multiteam.core.repository.TenantableRepository;
import com.multiteam.modules.patient.model.Reinforcer;
import org.springframework.stereotype.Repository;

@Repository
public interface ReinforcerRepository extends TenantableRepository<Reinforcer> {
}
