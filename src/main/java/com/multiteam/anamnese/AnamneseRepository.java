package com.multiteam.anamnese;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnamneseRepository extends TenantableRepository<Anamnese> {
}
