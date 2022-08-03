package com.shawn.study.deep.in.java.web.listener;

import com.shawn.study.deep.in.java.baens.core.ComponentContext;
import com.shawn.study.deep.in.java.web.entity.LoginUser;
import com.shawn.study.deep.in.java.web.orm.repository.UserRepository;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestingListener implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(TestingListener.class.getName());

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext sc = sce.getServletContext();
    ComponentContext componentContext = (ComponentContext) sc.getAttribute("COMPONENT_CONTEXT");
    testJNDIEnv(componentContext);
    //    testJpa(componentContext);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}

  private void testJpa(ComponentContext componentContext) {
    UserRepository userRepository =
        componentContext.getComponent("bean/UserRepository", UserRepository.class);
    LoginUser loginUser = new LoginUser();
    loginUser.setUsername("jack");
    loginUser.setPassword("12345");
    userRepository.insert(loginUser);

    LoginUser user = userRepository.findById(1);
    LOGGER.log(
        Level.INFO,
        String.format(
            "user id: %s, user name: %s, password: %s",
            user.getId(), user.getUsername(), user.getPassword()));

    LoginUser login = userRepository.findByNameAndPassword("jack", "123456");
    if (login == null) {
      LOGGER.log(Level.INFO, "login failed");
    }
  }

  private void testJNDIEnv(ComponentContext componentContext) {
    Object maxValue = componentContext.getComponent("maxValue");
    System.out.println(maxValue);
  }
}
