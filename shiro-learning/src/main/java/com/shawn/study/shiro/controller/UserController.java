package com.shawn.study.shiro.controller;

import com.shawn.study.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

  public final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping("/public/toLoginPage")
  public String loginPage() {
    return "login.html";
  }

  @RequestMapping("/public/login")
  public void login(String username, String password, Model model) {
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
    try {
      subject.login(token);
      model.addAttribute("msg", "success login");
    } catch (UnknownAccountException e) {
      e.printStackTrace();
      model.addAttribute("msg", "username error");
    } catch (IncorrectCredentialsException e) {
      e.printStackTrace();
      model.addAttribute("msg", "password error");
    }
  }
}
