package com.shawn.study.deep.in.java.configuration.source;

import java.util.Collections;
import java.util.Map;

public class JavaSystemEnvironmentConfigSource extends MapConfigSource {

  private static final String JAVA_SYSTEM_ENVIRONMENT = "java_system_environment";
  private static final Map<String, String> JAVA_SYSTEM_ENVIRONMENT_CONFIG;

  static {
    Map<String, String> env = System.getenv();
    JAVA_SYSTEM_ENVIRONMENT_CONFIG = Collections.unmodifiableMap(env);
  }

  public JavaSystemEnvironmentConfigSource() {
    super(
        JAVA_SYSTEM_ENVIRONMENT,
        JAVA_SYSTEM_ENVIRONMENT_CONFIG,
        ConfigSourceOrdinal.JAVA_SYSTEM_ENVIRONMENT_ORDINAL.getOrdinal());
  }
}
