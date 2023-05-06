package com.multiteam.clinic;

import com.multiteam.core.repositories.TenantableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicRepository extends TenantableRepository<Clinic> {

}
