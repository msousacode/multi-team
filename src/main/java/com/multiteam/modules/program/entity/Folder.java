package com.multiteam.modules.program.entity;

import com.multiteam.core.models.Auditable;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.treatment.Treatment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
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

    @Column(name = "code", insertable = false, updatable = false)
    private Integer code;

    @Column(name = "folder_name")
    private String folderName;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id")
    private Treatment treatment;

    @ManyToMany
    @JoinTable(
            name = "folders_professionals",
            joinColumns = @JoinColumn(name = "folder_id"),
            inverseJoinColumns = @JoinColumn(name = "folders_professionals_id"))
    private List<FolderProfessional> folderProfessional;
}
