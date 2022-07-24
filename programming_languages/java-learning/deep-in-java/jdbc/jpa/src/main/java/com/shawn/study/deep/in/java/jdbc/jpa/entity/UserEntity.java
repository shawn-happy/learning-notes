package com.shawn.study.deep.in.java.jdbc.jpa.entity;

import com.shawn.study.deep.in.java.jdbc.jpa.serialize.StringListSerializer;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "user")
@NamedQuery(
    name = "findByPhoneNumber",
    query = "select u from UserEntity u where u.phoneNumber = :phoneNumber")
@NamedQueries(
    value = {
      @NamedQuery(
          name = "findByEmail",
          query = "select u from UserEntity  u where u.email = :email"),
      @NamedQuery(name = "findByName", query = "select u from UserEntity  u where u.name = :name"),
      @NamedQuery(
          name = "findByNameAndPassword",
          query = "select u from UserEntity  u where u.name = :name and u.password = :password")
    })
public class UserEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column private String name;

  @Column private String password;

  @Column private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "birth_date")
  @Temporal(TemporalType.DATE)
  private Date birth;

  @Transient private int age;

  @Lob
  @Convert(converter = StringListSerializer.class)
  private List<String> hobbies;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @Column(columnDefinition = "boolean default true")
  private boolean locked;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Date getBirth() {
    return birth;
  }

  public void setBirth(Date birth) {
    this.birth = birth;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public List<String> getHobbies() {
    return hobbies;
  }

  public void setHobbies(List<String> hobbies) {
    this.hobbies = hobbies;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }
}
