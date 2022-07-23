package com.shawn.study.deep.in.java.jdbc.jpa;

import com.shawn.study.deep.in.java.jdbc.jpa.entity.AddressEntity;
import com.shawn.study.deep.in.java.jdbc.jpa.entity.Gender;
import com.shawn.study.deep.in.java.jdbc.jpa.entity.UserEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPADemo {

  public static void main(String[] args) {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("emf");
    EntityManager entityManager = factory.createEntityManager();

    UserEntity userEntity = new UserEntity();
    userEntity.setName("Shawn");
    userEntity.setPassword("123456");
    userEntity.setEmail("123456@126.com");
    userEntity.setPhoneNumber("12345678901");
    userEntity.setBirth(new Date());
    userEntity.setAge(28);
    userEntity.setGender(Gender.MALE);
    List<String> hobbies = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      hobbies.add("hobby_" + i);
    }
    userEntity.setHobbies(hobbies);

    AddressEntity addressEntity = new AddressEntity();
    addressEntity.setAddress("SHANGHAI");
    userEntity.setAddress(addressEntity);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    entityManager.persist(userEntity);
    transaction.commit();
    UserEntity user = entityManager.find(UserEntity.class, 1);
    System.out.println(user.getName());
    System.out.println(user.getHobbies());
    System.out.println(user.getAddress().getAddress());
  }
}
