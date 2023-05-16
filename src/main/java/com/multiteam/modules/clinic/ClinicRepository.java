package com.multiteam.modules.clinic;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends TenantableRepository<Clinic> {

}
