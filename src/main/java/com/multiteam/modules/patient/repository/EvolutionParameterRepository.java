package com.multiteam.modules.patient.repository;

import com.multiteam.core.repository.TenantableRepository;
import com.multiteam.modules.patient.model.EvolutonParameter;
import org.springframework.stereotype.Repository;

@Repository
public interface EvolutionParameterRepository extends TenantableRepository<EvolutonParameter> {
}
