package com.multiteam.modules.schedule;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends TenantableRepository<Schedule> {
}
