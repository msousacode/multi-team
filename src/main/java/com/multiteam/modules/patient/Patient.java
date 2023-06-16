package com.multiteam.modules.patient;

import com.multiteam.core.enums.SexEnum;
import com.multiteam.core.models.Tenantable;
import com.multiteam.modules.user.User;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.util.Assert;

@Entity
@Table(name = "patients")
public class Patient extends Tenantable {

  @Id
  @GeneratedValue
  @Column(name = "patient_id")
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "sex")
  @Enumerated(EnumType.STRING)
  private SexEnum sex;

  @Column(name = "cpf")
  private String cpf;

  @Column(name = "date_birth")
  private LocalDate dateBirth;

  @Column(name = "age")
  private Integer age;

  @Column(name = "active")
  private boolean active;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "cell_phone")
  private String cellPhone;

  public Patient() {
  }

  private Patient(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.sex = builder.sex;
    this.age = builder.age;
    this.active = builder.active;
    this.dateBirth = builder.dateBirth;
    this.user = builder.user;
    this.cellPhone = builder.cellPhone;
    this.cpf = builder.cpf;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public SexEnum getSex() {
    return sex;
  }

  public Integer getAge() {
    return age;
  }

  public boolean isActive() {
    return active;
  }

  public LocalDate getDateBirth() {
    return dateBirth;
  }

  public User getUser() {
    return user;
  }

  public String getCellPhone() {
    return cellPhone;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSex(SexEnum sex) {
    this.sex = sex;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setDateBirth(LocalDate dateBirth) {
    this.dateBirth = dateBirth;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setCellPhone(String cellPhone) {
    this.cellPhone = cellPhone;
  }

  public static class Builder {

    //mandatory
    private UUID id;
    private final String name;
    private final String cpf;
    private final SexEnum sex;
    private final Integer age;
    private boolean active;
    private final LocalDate dateBirth;
    private final User user;

    //optional
    private String cellPhone;

    public Builder(final String name, final SexEnum sex, final String cpf, final Integer age,
        final LocalDate dateBirth, final User user) {

      Assert.notNull(dateBirth, "value dateBirth cannot be null");
      Assert.notNull(user, "user cannot be null");
      Assert.notNull(cpf, "cpf cannot be null");
      Assert.isTrue(!name.isEmpty(), "patient name cannot be empty");
      Assert.notNull(sex, "patient sex cannot be null");
      Assert.notNull(age, "patient age cannot be null");

      this.name = name;
      this.sex = sex;
      this.cpf = cpf;
      this.age = age;
      this.dateBirth = dateBirth;
      this.user = user;
    }

    public Builder id(final UUID id) {
      this.id = id;
      return this;
    }

    public Builder active(boolean active) {
      this.active = active;
      return this;
    }

    public Builder cellPhone(String cellPhone) {
      this.cellPhone = cellPhone;
      return this;
    }

    public Patient build() {
      return new Patient(this);
    }
  }

  @Override
  public String toString() {
    return "Patient{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}