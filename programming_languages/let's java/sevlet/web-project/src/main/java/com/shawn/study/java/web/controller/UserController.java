package com.shawn.study.java.web.controller;

import com.shawn.study.java.web.HttpMethod;
import com.shawn.study.java.web.annotation.Controller;
import com.shawn.study.java.web.annotation.RequestBody;
import com.shawn.study.java.web.annotation.RequestMapping;
import com.shawn.study.java.web.annotation.ResponseBody;
import com.shawn.study.java.web.dto.LoginRequestDTO;
import com.shawn.study.java.web.dto.RegisterRequestDTO;
import com.shawn.study.java.web.dto.Response;
import com.shawn.study.java.web.dto.UpdatePasswordDTO;
import com.shawn.study.java.web.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

  private static List<UserDTO> userDTOList = new ArrayList<>();

  private static int id = 0;

  private static final String DEFAULT_PASSWORD = "123456";
  /** @param registerRequestDTO user registering dto */
  @RequestMapping(method = HttpMethod.POST, value = "/register")
  @ResponseBody
  public Response<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
    String address = registerRequestDTO.getAddress();
    String username = registerRequestDTO.getUsername();
    UserDTO userDTO = new UserDTO(++id, username, DEFAULT_PASSWORD, address);
    userDTOList.add(userDTO);
    return Response.ok("注册成功");
  }

  @RequestMapping(method = HttpMethod.GET, value = "/findById")
  @ResponseBody
  public Response<UserDTO> findById(Integer id) {
    return Response.ok(
        userDTOList.stream().filter(userDTO -> userDTO.getId() == id).findFirst().orElse(null));
  }

  @RequestMapping(method = HttpMethod.GET, value = "/login")
  @ResponseBody
  public Response<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
    UserDTO userDTO =
        userDTOList
            .stream()
            .filter(
                user ->
                    user.getUsername().equals(loginRequestDTO.getUsername())
                        && user.getPassword().equals(loginRequestDTO.getPassword()))
            .findFirst()
            .orElse(null);
    if (userDTO == null) {
      return Response.error("用户名或密码错误");
    }
    return Response.ok("登录成功");
  }

  @RequestMapping(method = HttpMethod.PUT, value = "/update/password")
  @ResponseBody
  public Response<String> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
    int id = updatePasswordDTO.getId();
    UserDTO userDTO =
        userDTOList.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    if (userDTO == null) {
      return Response.error("该用户不存在");
    }
    String password = userDTO.getPassword();
    if (password.equals(updatePasswordDTO.getNewPassword())) {
      return Response.error("新旧密码一样");
    }
    userDTO.setPassword(updatePasswordDTO.getNewPassword());
    return Response.ok("修改密码成功");
  }
}
