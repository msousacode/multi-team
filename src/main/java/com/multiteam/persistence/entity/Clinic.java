package com.multiteam.persistence.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Id
    @GeneratedValue
    @Column(name = "clinic_id")
    private UUID id;

    @Column(name = "clinic_name")
    private String clinicName;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    @Column(name = "email")
    private String email;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "observation")
    private String observation;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Column(name = "removed_date")
    LocalDateTime removedDate;
}
