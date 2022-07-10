package com.shawn.study.deep.in.java.web.controller;

import com.shawn.study.deep.in.java.web.mvc.annotation.Controller;
import com.shawn.study.deep.in.java.web.mvc.annotation.RequestBody;
import com.shawn.study.deep.in.java.web.mvc.annotation.RequestMapping;
import com.shawn.study.deep.in.java.web.mvc.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/index")
public class HelloWorldController {

  @ResponseBody
  @RequestMapping("/list")
  public Map<String, Object> list(@RequestBody User user) {
    return Map.of("username", user.getUsername(), "password", user.getPassword());
  }

  @RequestMapping("/page")
  public String page() {
    return "test";
  }
}
