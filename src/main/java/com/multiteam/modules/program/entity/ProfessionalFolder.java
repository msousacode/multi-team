package com.multiteam.modules.program.entity;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.patient.model.Patient;
import com.multiteam.modules.professional.Professional;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "folders_professionals")
public class ProfessionalFolder {

    @Id
    @GeneratedValue
    @Column(name = "folders_professionals_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "professional_id", referencedColumnName = "professional_id", nullable = false)
    private Professional professional;

    @Column(name = "situation")
    @Enumerated(EnumType.STRING)
    private SituationEnum situation;
}
