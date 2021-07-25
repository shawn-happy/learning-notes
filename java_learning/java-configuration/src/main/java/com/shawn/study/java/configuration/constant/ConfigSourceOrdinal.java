package com.shawn.study.java.configuration.constant;

/**
 * order config source. the smaller the ordinal value, the higher the priority
 *
 * @author Shawn
 * @since 1.0.1
 */
public enum ConfigSourceOrdinal {
  IN_MEMORY_ORDINAL(1),
  CLASSPATH_PROPERTIES_ORDINAL(2),
  CLASSPATH_YAML_ORDINAL(3),
  CLASSPATH_XML_ORDINAL(4),
  CLASSPATH_JSON_ORDINAL(5),
  DATABASE_ORDINAL(6),
  JAVA_SYSTEM_PROPERTIES_ORDINAL(7),
  JAVA_SYSTEM_ENVIRONMENT_ORDINAL(8),
  EXTRA_PROPERTIES_ORDINAL(9),
  EXTRA_YAML_ORDINAL(10),
  EXTRA_XML_ORDINAL(11),
  EXTRA_JSON_ORDINAL(12),
  ;
  private int ordinal;

  ConfigSourceOrdinal(int ordinal) {
    this.ordinal = ordinal;
  }

  public int getOrdinal() {
    return ordinal;
  }
}
