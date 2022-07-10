package com.shawn.study.deep.in.java.web.servlet;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class ServletContextDemo {

  private static final Logger log =
      LogManager.getLogManager().getLogger(ServletContextDemo.class.getName());

  public static void initConfig(ServletContext context) {
    Enumeration<String> initParameterNames = context.getInitParameterNames();
    while (initParameterNames.hasMoreElements()) {
      String name = initParameterNames.nextElement();
      String value = context.getInitParameter(name);
      log.info(String.format("init config name: %s, init config value: %s", name, value));
    }
  }

  public static void attributes(ServletContext context) {
    Enumeration<String> attributeNames = context.getAttributeNames();
    while (attributeNames.hasMoreElements()) {
      String name = attributeNames.nextElement();
      Object attribute = context.getAttribute(name);
      log.info(String.format("attribute name: %s, attribute value: %s", name, attribute));
    }
  }

  public static void versions(ServletContext context) {
    int majorVersion = context.getMajorVersion();
    int minorVersion = context.getMinorVersion();
    int effectiveMajorVersion = context.getEffectiveMajorVersion();
    int effectiveMinorVersion = context.getEffectiveMinorVersion();
    log.info(
        String.format(
            "major version: %s, effective major version: %s, minor version: %s, effective minor version: %s, server info: %s",
            majorVersion,
            effectiveMajorVersion,
            minorVersion,
            effectiveMinorVersion,
            context.getServerInfo()));
  }

  public static void registration(ServletContext context) {
    Map<String, ? extends FilterRegistration> filterRegistrations =
        context.getFilterRegistrations();
    filterRegistrations.forEach(
        (name, value) -> {
          FilterRegistration filterRegistration = context.getFilterRegistration(name);
          assert filterRegistration == value;
          Collection<String> urlPatternMappings = filterRegistration.getUrlPatternMappings();
          Collection<String> servletNameMappings = filterRegistration.getServletNameMappings();
          log.info(
              String.format(
                  "url pattern mappings: %s, servlet name mappings: %s",
                  urlPatternMappings, servletNameMappings));
        });
    Map<String, ? extends ServletRegistration> servletRegistrations =
        context.getServletRegistrations();
    servletRegistrations.forEach(
        (name, value) -> {
          ServletRegistration servletRegistration = context.getServletRegistration(name);
          assert servletRegistration == value;
          Collection<String> mappings = servletRegistration.getMappings();
          log.info(String.format("servlet name: %s, mappings: %s", name, mappings));
        });
  }

  public static void log(ServletContext context, String message) {
    context.log(message);
  }
}
