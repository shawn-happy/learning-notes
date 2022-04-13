package com.shawn.study.deep.in.java.web.servlet;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServletContextDemo {

  private static final Logger log = LogManager.getLogger(ServletContextDemo.class);

  public static void initConfig(ServletContext context) {
    Enumeration<String> initParameterNames = context.getInitParameterNames();
    while (initParameterNames.hasMoreElements()) {
      String name = initParameterNames.nextElement();
      String value = context.getInitParameter(name);
      log.info("init config name: {}, init config value: {}", name, value);
    }
  }

  public static void attributes(ServletContext context) {
    Enumeration<String> attributeNames = context.getAttributeNames();
    while (attributeNames.hasMoreElements()) {
      String name = attributeNames.nextElement();
      Object attribute = context.getAttribute(name);
      log.info("attribute name: {}, attribute value: {}", name, attribute);
    }
  }

  public static void versions(ServletContext context) {
    int majorVersion = context.getMajorVersion();
    int minorVersion = context.getMinorVersion();
    int effectiveMajorVersion = context.getEffectiveMajorVersion();
    int effectiveMinorVersion = context.getEffectiveMinorVersion();
    log.info(
        "major version: {}, effective major version: {}, minor version: {}, effective minor version: {}, server info: {}",
        majorVersion,
        effectiveMajorVersion,
        minorVersion,
        effectiveMinorVersion,
        context.getServerInfo());
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
              "url pattern mappings: {}, servlet name mappings: {}",
              urlPatternMappings,
              servletNameMappings);
        });
    Map<String, ? extends ServletRegistration> servletRegistrations =
        context.getServletRegistrations();
    servletRegistrations.forEach(
        (name, value) -> {
          ServletRegistration servletRegistration = context.getServletRegistration(name);
          assert servletRegistration == value;
          Collection<String> mappings = servletRegistration.getMappings();
          log.info("servlet name: {}, mappings: {}", name, mappings);
        });
  }

  public static void log(ServletContext context, String message) {
    context.log(message);
  }
}
