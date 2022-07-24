package com.shawn.study.deep.in.java.web.listener;

import com.shawn.study.deep.in.java.baens.core.DefaultComponentContext;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BeanContextListener implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(BeanContextListener.class.getName());

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();
    DefaultComponentContext componentContext = new DefaultComponentContext();
    servletContext.setAttribute("COMPONENT_CONTEXT", componentContext);
    List<String> componentNames = componentContext.getComponentNames();
    LOGGER.info("component names: " + componentNames);
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();
    DefaultComponentContext componentContext =
        (DefaultComponentContext) servletContext.getAttribute("COMPONENT_CONTEXT");
    componentContext.destroy();
  }
}
