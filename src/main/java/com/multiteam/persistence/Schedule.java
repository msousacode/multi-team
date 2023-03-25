package com.multiteam.persistence;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "schedule")
@AttributeOverride(name = "id", column = @Column(name = "schedule_id", nullable = false))
public class Schedule extends Audit {
}
