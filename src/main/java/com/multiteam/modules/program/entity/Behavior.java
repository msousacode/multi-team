package com.multiteam.modules.program.entity;

import com.multiteam.core.models.Tenantable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "behaviors")
public class Behavior extends Tenantable {

    @Id
    @GeneratedValue
    @Column(name = "behavior_id")
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
    @JoinColumn(name = "program_id", referencedColumnName = "program_id")
    private Program program;

    @Column(name = "active")
    private Boolean active;
}
