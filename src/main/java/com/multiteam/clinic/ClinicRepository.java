package com.multiteam.clinic;

import com.multiteam.core.repositories.TenantableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends TenantableRepository<Clinic> {
}
