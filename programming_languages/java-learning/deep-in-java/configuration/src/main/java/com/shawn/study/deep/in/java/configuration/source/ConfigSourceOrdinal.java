package com.shawn.study.deep.in.java.configuration.source;

public enum ConfigSourceOrdinal {
  DEFAULT_ORDINAL(1000),
  MEMORY_ORDINAL(900),
  PROPERTIES_ORDINAL(800),
  RESOURCE_ORDINAL(700),
  SERVLET_CONTEXT_ORDINAL(600),
  SERVLET_CONFIG_ORDINAL(500),
  JNDI_CONFIG_ORDINAL(400),
  JAVA_SYSTEM_PROPERTIES_ORDINAL(300),
  JAVA_SYSTEM_ENVIRONMENT_ORDINAL(200),
  ;

  private final int ordinal;

  ConfigSourceOrdinal(int ordinal) {
    this.ordinal = ordinal;
  }

  public int getOrdinal() {
    return ordinal;
  }
}
