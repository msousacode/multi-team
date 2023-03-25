package com.multiteam.persistence;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "user_id", nullable = false))
public class User extends Audit {
}
