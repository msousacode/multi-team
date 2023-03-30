package com.multiteam.persistence.repository;

import com.multiteam.persistence.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, UUID> {
    Set<Treatment> findAllByPatient_Id(UUID patientId);

    @Query("""
            select t from Treatment t
            join t.guests tg
            join t.patient p
            where tg.id = :guestId
            """)
    List<Treatment> getAllTreatmentsByGuestId(@Param("guestId") UUID guestId);
}
