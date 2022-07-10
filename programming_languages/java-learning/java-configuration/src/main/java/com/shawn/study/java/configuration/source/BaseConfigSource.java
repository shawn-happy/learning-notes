package com.shawn.study.java.configuration.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * Abstract the basic superclass for {@code ConfigSource} the super class Provide some common
 * operations
 *
 * @author Shawn
 * @since 1.0.1
 */
public abstract class BaseConfigSource implements ConfigSource {

  private String name;

  private int ordinal;

  protected BaseConfigSource(String name, int ordinal) {
    this.name = name;
    this.ordinal = ordinal;
  }

  @Override
  public int getOrdinal() {
    return ordinal;
  }

  @Override
  public String getName() {
    return name;
  }
}
