package com.shawn.study.deep.in.java.configuration.utils;

import com.shawn.study.deep.in.java.baens.core.ComponentContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ConfigSourceUtils {

  public static Map<String, String> toMap(Properties properties) {
    if (properties == null || properties.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<String, String> config = new HashMap<>();
    Set<Object> keySet = properties.keySet();
    for (Object key : keySet) {
      config.put(key.toString(), properties.getProperty(key.toString()));
    }
    return config;
  }

  public static Map<String, String> toMap(ServletConfig servletConfig) {
    if (servletConfig == null) {
      return Collections.emptyMap();
    }
    Enumeration<String> initParameterNames = servletConfig.getInitParameterNames();
    Map<String, String> config = new HashMap<>();
    while (initParameterNames.hasMoreElements()) {
      String name = initParameterNames.nextElement();
      String value = servletConfig.getInitParameter(name);
      config.put(name, value);
    }
    return config;
  }

  public static Map<String, String> toMap(ServletContext servletContext) {
    if (servletContext == null) {
      return Collections.emptyMap();
    }
    Enumeration<String> initParameterNames = servletContext.getInitParameterNames();
    Map<String, String> config = new HashMap<>();
    while (initParameterNames.hasMoreElements()) {
      String name = initParameterNames.nextElement();
      String value = servletContext.getInitParameter(name);
      config.put(name, value);
    }
    return config;
  }

  public static Map<String, String> toMap(ComponentContext componentContext) {
    if (componentContext == null) {
      return Collections.emptyMap();
    }
    List<String> componentNames = componentContext.getComponentNames();
    Map<String, String> config = new HashMap<>();
    for (String componentName : componentNames) {
      String value = componentContext.getComponent(componentName, String.class);
      config.put(componentName, value);
    }

    return config;
  }
}
