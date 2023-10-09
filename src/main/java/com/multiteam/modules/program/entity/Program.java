package com.multiteam.modules.program.entity;

import com.multiteam.core.models.Tenantable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "programs")
public class Program extends Tenantable {

    @Id
    @GeneratedValue
    @Column(name = "program_id")
    private UUID id;

    @Column(name = "code", insertable = false, updatable = false)//Faz o campo ser read-only
    private Integer code;

    @Column(name = "program_name")
    private String programName;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "ability")
    private Integer ability;

    @Column(name = "protocol")
    private Integer protocol;

    @Column(name = "targets_by_session")
    private Integer targetsBySession;

    @Column(name = "attempt_by_session")
    private Integer attemptBySession;

    @Column(name = "definition")
    private String definition;

    @Column(name = "procedure")
    private String procedure;

    @Column(name = "learning_criterion")
    private String learningCriterion;

    @Column(name = "materials")
    private String materials;

    @Column(name = "observation")
    private String observation;

    @Column(name = "evolution_quantity_by_session")
    private Integer evolutionQuantityBySession;

    @Column(name = "evolution_percentage_attempts")
    private Integer evolutionPercentageAttempts;

    @Column(name = "correction_quantity_by_session")
    private Integer correctionQuantityBySession;

    @Column(name = "correction_percentage_attempts")
    private Integer correctionPercentageAttempts;

    @Column(name = "active")
    private Boolean active;
}
