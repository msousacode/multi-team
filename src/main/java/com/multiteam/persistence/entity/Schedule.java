package com.multiteam.persistence.entity;

import com.multiteam.enums.ScheduleEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schedule")
public record Schedule(

        @Id
        @GeneratedValue
        @Column(name = "schedule_id")
        UUID id,

        @Column(name = "realization_date")
        LocalDateTime realizationDate,

        @Column(name = "situation")
        @Enumerated(EnumType.STRING)
        ScheduleEnum situation,

        @Column(name = "observation")
        String observation,

        @Column(name = "created_date")
        LocalDateTime createdDate,

        @Column(name = "removed_date")
        LocalDateTime removedDate,

        @Column(name = "updated_date")
        LocalDateTime updatedDate,

        @Column(name = "created_by")
        String createdBy,

        @Column(name = "modified_by")
        String modifiedBy
) {

}
