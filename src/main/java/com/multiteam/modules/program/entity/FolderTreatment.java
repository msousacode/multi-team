package com.multiteam.modules.program.entity;

import com.multiteam.modules.treatment.Treatment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "folders_treatments")
public class FolderTreatment {

    @Id
    @GeneratedValue
    @Column(name = "folders_treatments_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id", nullable = false)
    private Treatment treatment;

    public FolderTreatment(Folder folder, Treatment treatment) {
        this.folder = folder;
        this.treatment = treatment;
    }

    public static FolderTreatment getInstance(Folder folder, Treatment treatment) {
        return new FolderTreatment(folder, treatment);
    }
}
