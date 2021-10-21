package com.shawn.study.java.web;

import com.shawn.study.java.web.controller.UserController;
import com.shawn.study.java.web.dto.LoginRequestDTO;
import com.shawn.study.java.web.dto.RegisterRequestDTO;
import com.shawn.study.java.web.dto.Response;
import com.shawn.study.java.web.dto.UpdatePasswordDTO;
import com.shawn.study.java.web.dto.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserControllerTests {

  private UserController userController;

  @Before
  public void setup() {
    userController = new UserController();
  }

  @Test
  public void register() {
    RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("Shawn", "SHANGHAI");
    Assert.assertEquals(0, userController.register(registerRequestDTO).getCode());

    registerRequestDTO = new RegisterRequestDTO("Jack", "Beijing");
    Assert.assertEquals(0, userController.register(registerRequestDTO).getCode());

    registerRequestDTO = new RegisterRequestDTO("Bob", "Hangzhou");
    Assert.assertEquals(0, userController.register(registerRequestDTO).getCode());
  }

  @Test
  public void findById() {
    Response<UserDTO> response = userController.findById(1);
    Assert.assertEquals("Shawn", response.getData().getUsername());
    response = userController.findById(2);
    Assert.assertEquals("123456", response.getData().getPassword());
    response = userController.findById(3);
    Assert.assertEquals("Hangzhou", response.getData().getAddress());
  }

  @Test
  public void findByIdIsNull() {
    Response<UserDTO> response = userController.findById(4);
    Assert.assertNull(response.getData());
  }

  @Test
  public void login() {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("Shawn", "123456");
    Response<String> response = userController.login(loginRequestDTO);
    Assert.assertEquals(0, response.getCode());
  }

  @Test
  public void loginError() {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("Shawn", "123457");
    Response<String> response = userController.login(loginRequestDTO);
    Assert.assertEquals(1, response.getCode());
  }

  @Test
  public void updatePassword() {
    UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO(1, "123457");
    Response<String> response = userController.updatePassword(updatePasswordDTO);
    Assert.assertEquals(0, response.getCode());
  }

  @Test
  public void updatePasswordErrorWithSamePwd() {
    UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO(2, "123456");
    Response<String> response = userController.updatePassword(updatePasswordDTO);
    Assert.assertEquals(1, response.getCode());
    Assert.assertEquals("新旧密码一样", response.getMessage());
  }

  @Test
  public void updatePasswordErrorWithNoId() {
    UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO(5, "123457");
    Response<String> response = userController.updatePassword(updatePasswordDTO);
    Assert.assertEquals(1, response.getCode());
    Assert.assertEquals("该用户不存在", response.getMessage());
  }
}
