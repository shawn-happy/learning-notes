package com.shawn.study.java.rest.service;

import com.shawn.study.java.rest.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;

public class UserService {

  private static List<UserDTO> userDTOList = new ArrayList<>();

  static {
    userDTOList.add(new UserDTO(1, "Shawn", "ShangHai", "12345678901"));
    userDTOList.add(new UserDTO(2, "Jack", "BEIJING", "12345678902"));
    userDTOList.add(new UserDTO(3, "Jackson", "NANJING", "12345678903"));
    userDTOList.add(new UserDTO(4, "John", "HANGZHOU", "12345678904"));
    userDTOList.add(new UserDTO(5, "Johnson", "SHANGHAI", "12345678905"));
  }

  public List<UserDTO> getUserDTOList() {
    return userDTOList;
  }
}
