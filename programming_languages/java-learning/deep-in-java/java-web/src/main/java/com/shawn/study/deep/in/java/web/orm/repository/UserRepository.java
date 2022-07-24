package com.shawn.study.deep.in.java.web.orm.repository;

import com.shawn.study.deep.in.java.web.entity.LoginUser;

public interface UserRepository {

  void insert(LoginUser userEntity);

  LoginUser findById(int id);

  LoginUser findByNameAndPassword(String name, String password);
}
