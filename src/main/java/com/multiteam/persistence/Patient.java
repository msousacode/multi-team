package com.multiteam.persistence;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "patients")
@AttributeOverride(name = "id", column = @Column(name = "patient_id", nullable = false))
public class Patient extends Audit {
}
