package com.shawn.study.deep.in.java.web.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class BeanContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      Context context = new InitialContext();
      Context evnContext = (Context) context.lookup("java:comp/env");
      NamingEnumeration<NameClassPair> list = evnContext.list("/");
      DataSource dataSource = (DataSource) evnContext.lookup("java/TestDB");
      System.out.println(dataSource);
    } catch (NamingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {}
}
