package com.multiteam.modules.program.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "folders_programs")
public class FolderProgram {

    @Id
    @GeneratedValue
    @Column(name = "folders_programs_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "program_id", nullable = false)
    private Program program;
}
