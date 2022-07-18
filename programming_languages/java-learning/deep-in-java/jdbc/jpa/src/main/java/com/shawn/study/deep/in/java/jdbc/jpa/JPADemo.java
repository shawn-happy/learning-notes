package com.shawn.study.deep.in.java.jdbc.jpa;

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
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    entityManager.persist(userEntity);
    transaction.commit();
    UserEntity user = entityManager.find(UserEntity.class, 1);
    System.out.println(user.getName());
  }
}
