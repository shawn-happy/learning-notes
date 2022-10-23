package com.shawn.study.shiro.service;

import com.shawn.study.shiro.entity.User;
import java.util.List;

public interface UserService {

  User getUserByUsername(String username);

  List<String> getRolesByUser(String username);

  List<String> getPermissionsByUser(String username);
}
