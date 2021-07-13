package com.shawn.study.java.web.dao;

import com.shawn.study.java.web.entity.UserEntity;
import java.util.List;

public interface UserDao {

  int insert(UserEntity userEntity);

  List<UserEntity> findAll();

  UserEntity findById(int id);

  List<UserEntity> findByAddress(String address);

  int updatePasswordById(int id, String password);

  int deleteById(int id);

  int count();
}
