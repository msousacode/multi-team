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

  @Query("FROM Schedule s WHERE s.professional.id = :professionalId AND s.status in ('AGENDADO', 'CONFIRMADO') AND (s.start >= :start or s.end <= :end) AND s.active = true")
  List<Schedule> findAllScheduleOfProfessional(UUID professionalId, LocalDateTime start,
      LocalDateTime end);
}
