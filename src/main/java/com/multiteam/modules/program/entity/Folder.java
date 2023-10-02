package com.multiteam.modules.program.entity;

import com.multiteam.core.models.Auditable;
import com.multiteam.core.models.Tenantable;
import com.multiteam.modules.patient.model.Patient;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "folders")
public class Folder extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "folder_id")
    private UUID id;

    @Column(name = "folder_name")
    private String folderName;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    private Patient patient;
}
