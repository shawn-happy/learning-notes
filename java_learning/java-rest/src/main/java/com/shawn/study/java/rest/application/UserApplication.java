package com.shawn.study.java.rest.application;

import com.shawn.study.java.rest.controller.UserController;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/jax/rs")
public class UserApplication extends Application {

  private final Set<Object> userControllers = new HashSet<>();

  public UserApplication() {
    userControllers.add(new UserController());
  }

  @Override
  public Set<Class<?>> getClasses() {
    return super.getClasses();
  }

  @Override
  public Set<Object> getSingletons() {
    return userControllers;
  }

  @Override
  public Map<String, Object> getProperties() {
    return super.getProperties();
  }
}
