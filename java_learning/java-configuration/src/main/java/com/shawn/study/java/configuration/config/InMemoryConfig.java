package com.shawn.study.java.configuration.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryConfig {

  private static final Map<String, String> configMap = new HashMap<>();

  static {
    configMap.put("application.name", "In Memory Config");
  }

  public static Map<String, String> getConfigMap() {
    return Collections.unmodifiableMap(configMap);
  }
}
