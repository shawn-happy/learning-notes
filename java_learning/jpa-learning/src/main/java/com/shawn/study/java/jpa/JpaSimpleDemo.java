package com.shawn.study.java.jpa;

import com.shawn.study.java.jpa.entity.UserEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaSimpleDemo {

  public static void main(String[] args) {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("emf");
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("Shawn");
    userEntity.setPassword("123456");
    userEntity.setAge(26);
    userEntity.setAddress("ShangHai");
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    entityManager.persist(userEntity);
    transaction.commit();

    System.out.println(entityManager.find(UserEntity.class, 1));
  }
}
