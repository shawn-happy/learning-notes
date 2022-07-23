package com.shawn.study.deep.in.java.jdbc.jpa.repository;

import com.shawn.study.deep.in.java.jdbc.jpa.entity.UserEntity;

public class UserRepository extends DefaultJpaRepository<UserEntity, Integer> {

  public UserRepository(String persistenceUnitName) {
    super(UserEntity.class, persistenceUnitName);
  }
}
