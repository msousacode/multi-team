package com.multiteam.modules.schedule;

import com.multiteam.core.repository.TenantableRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends TenantableRepository<Schedule> {

    List<Schedule> findAllByClinicIdAndActiveIsTrue(@Param("clinicId") UUID clinicId);
}
