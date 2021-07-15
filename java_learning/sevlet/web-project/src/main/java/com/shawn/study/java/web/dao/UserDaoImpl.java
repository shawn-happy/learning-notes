package com.shawn.study.java.web.dao;

import com.shawn.study.java.web.entity.UserEntity;
import java.util.List;

public class UserDaoImpl implements UserDao {

  @Override
  public int insert(UserEntity userEntity) {
    return 0;
  }

  @Override
  public List<UserEntity> findAll() {
    return null;
  }

  @Override
  public UserEntity findById(int id) {
    return null;
  }

  @Override
  public List<UserEntity> findByAddress(String address) {
    return null;
  }

  @Override
  public int updatePasswordById(int id, String password) {
    return 0;
  }

  @Override
  public int deleteById(int id) {
    return 0;
  }

  @Override
  public int count() {
    return 0;
  }
}
