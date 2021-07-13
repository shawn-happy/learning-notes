package com.shawn.study.java.web.listener;

import com.shawn.study.java.web.context.JndiApplicationContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * application context listener for jndi
 *
 * @author Shawn
 * @since 1.0.0
 */
public class ApplicationContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
    JndiApplicationContext jndiApplicationContext = new JndiApplicationContext();
    jndiApplicationContext.init(servletContext);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    JndiApplicationContext instance = JndiApplicationContext.getInstance();
    if (instance != null) {
      instance.destroy();
    }
  }
}
