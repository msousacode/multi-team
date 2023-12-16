package com.multiteam.modules.program.entity;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.modules.professional.Professional;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "folders_professionals")
public class FolderProfessional {

    @Id
    @GeneratedValue
    @Column(name = "folders_professionals_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", referencedColumnName = "professional_id", nullable = false)
    private Professional professional;

    @Column(name = "situation")
    @Enumerated(EnumType.STRING)
    private SituationEnum situation;

    public FolderProfessional(){}

    public FolderProfessional(Folder folder) {
        this.folder = folder;
    }
}
