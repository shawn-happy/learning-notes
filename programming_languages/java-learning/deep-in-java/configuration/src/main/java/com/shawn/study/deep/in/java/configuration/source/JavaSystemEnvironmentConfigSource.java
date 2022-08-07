package com.shawn.study.deep.in.java.configuration.source;

import java.util.Collections;
import java.util.Map;

public class JavaSystemEnvironmentConfigSource extends MapConfigSource {

  private static final String JAVA_SYSTEM_ENVIRONMENT = "java.system.environment";
  private static final Map<String, String> JAVA_SYSTEM_ENVIRONMENT_CONFIG;

  static {
    Map<String, String> env = System.getenv();
    JAVA_SYSTEM_ENVIRONMENT_CONFIG = Collections.unmodifiableMap(env);
  }

  public JavaSystemEnvironmentConfigSource() {
    super(
        JAVA_SYSTEM_ENVIRONMENT, ConfigSourceOrdinal.JAVA_SYSTEM_ENVIRONMENT_ORDINAL.getOrdinal());
    super.prepareConfig(JAVA_SYSTEM_ENVIRONMENT_CONFIG);
  }
}
