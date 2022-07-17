package com.shawn.study.deep.in.java.jndi.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BeanContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    //    DefaultBeanContext beanContext = new DefaultBeanContext();
    //    beanContext.init();
    //    List<String> beanNames = beanContext.getBeanNames();
    //    servletContextEvent.getServletContext().log(beanNames.toString());
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {}
}
