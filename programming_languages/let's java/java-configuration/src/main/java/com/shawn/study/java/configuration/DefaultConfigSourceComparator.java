package com.shawn.study.java.configuration;

import java.util.Comparator;
import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * singleton design pattern The default sort implementation for Multi-ConfigSource
 *
 * @author Shawn
 * @since 1.0.0
 */
public class DefaultConfigSourceComparator implements Comparator<ConfigSource> {

  public static final Comparator<ConfigSource> INSTANCE = new DefaultConfigSourceComparator();

  private DefaultConfigSourceComparator() {}

  @Override
  public int compare(ConfigSource o1, ConfigSource o2) {
    return Integer.compare(o1.getOrdinal(), o2.getOrdinal());
  }
}
