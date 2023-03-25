package com.multiteam.persistence;

import com.multiteam.persistence.enuns.SexEnuns;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
@AttributeOverride(name = "id", column = @Column(name = "patient_id", nullable = false))
public class Patient extends Audit {

    @NotEmpty(message = "invalid name format")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "")
    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private SexEnuns sex;

    @NotEmpty(message = "invalid name format")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    public String getName() {
        return name;
    }

    public SexEnuns getSex() {
        return sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
