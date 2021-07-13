package com.shawn.study.java.web.listener;

import com.shawn.study.java.web.context.JndiApplicationContext;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 * unit testing
 *
 * @author Shawn
 * @since 1.0.0
 */
public class TestingListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    JndiApplicationContext instance = JndiApplicationContext.getInstance();
    DataSource dataSource = instance.getComponent("jdbc/datasource");
    try {
      Connection connection = dataSource.getConnection();
      sce.getServletContext().log("Connected to database successfully!");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}
}
