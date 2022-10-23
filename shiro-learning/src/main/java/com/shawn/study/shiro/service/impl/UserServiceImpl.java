package com.shawn.study.shiro.service.impl;

import com.shawn.study.shiro.dao.UserRepository;
import com.shawn.study.shiro.entity.User;
import com.shawn.study.shiro.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public List<String> getRolesByUser(String username) {
    User user = getUserByUsername(username);
    String roles = user.getRoles();
    return Arrays.asList(roles.split(","));
  }

  @Override
  public List<String> getPermissionsByUser(String username) {
    User user = getUserByUsername(username);
    String roles = user.getPermissions();
    return Arrays.asList(roles.split(","));
  }
}
