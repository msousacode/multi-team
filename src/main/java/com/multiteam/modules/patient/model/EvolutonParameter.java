package com.multiteam.modules.patient.model;

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
@Table(name = "parameters")
public class EvolutonParameter extends Tenantable {

    @Id
    @GeneratedValue
    @Column(name = "parameter_id")
    private UUID id;

    @Column(name = "description")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private Patient patient;
}