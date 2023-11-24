package com.multiteam.modules.program.entity;

import com.multiteam.core.models.Tenantable;
import com.multiteam.modules.patient.model.Patient;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "behaviors_collect")
public class BehaviorCollect extends Tenantable {

    @Id
    @GeneratedValue
    @Column(name = "behavior_collect_id")
    private UUID id;

    @Column(name = "behavior_name")
    private String behaviorName;

    @Column(name = "order_execution")
    private Integer orderExecution;

    @Column(name = "situation")
    private Integer situation;

    @Column(name = "observation")
    private String observation;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "acquired_date")
    private LocalDateTime acquiredDate;

    @Column(name = "maintenance_count")
    private Integer maintenanceCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "behavior_id", referencedColumnName = "behavior_id")
    private Behavior behavior;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "collect_time", columnDefinition = "false")
    private Boolean time;

    @Column(name = "question")
    private String question;

    @Column(name = "response")
    private String response;

    @Column(name = "collection_date")
    private LocalDateTime collectionDate;

    @Column(name = "program_id", updatable = false)
    private UUID programId;
}
