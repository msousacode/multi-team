package com.multiteam.modules.schedule;

import com.multiteam.core.repository.TenantableRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends TenantableRepository<Schedule> {

  List<Schedule> findAllByClinicIdAndActiveIsTrue(@Param("clinicId") UUID clinicId);

  @Modifying
  @Query("UPDATE Schedule s SET s.active = false WHERE s.id = :scheduleId AND s.tenantId = :tenantId")
  void inactiveScheduleById(UUID scheduleId, UUID tenantId);

  @Query("SELECT COUNT(1) FROM Schedule s WHERE s.professional.id = :professionalId AND s.start >= :start AND s.end <= :end")
  Integer findAllScheduleOfProfessional(UUID professionalId, LocalDateTime start,
      LocalDateTime end);
}
