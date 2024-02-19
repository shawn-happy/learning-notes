package com.shawn.study.deep.in.spring.boot.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Profile("dev")
public class DevController {
  @GetMapping("/echo/{message}")
  public String echo(@PathVariable String message) {
    return "dev: " + message;
  }

  @RequestMapping(
      value = "/test",
      method = {RequestMethod.GET, RequestMethod.POST},
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public User test(User user) {
    return user;
  }
}
