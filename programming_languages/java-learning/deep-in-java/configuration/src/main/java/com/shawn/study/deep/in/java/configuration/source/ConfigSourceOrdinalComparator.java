package com.shawn.study.deep.in.java.configuration.source;

import java.util.Comparator;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class ConfigSourceOrdinalComparator implements Comparator<ConfigSource> {

  /** Singleton instance {@link ConfigSourceOrdinalComparator} */
  public static final Comparator<ConfigSource> INSTANCE = new ConfigSourceOrdinalComparator();

  private ConfigSourceOrdinalComparator() {}

  @Override
  public int compare(ConfigSource o1, ConfigSource o2) {
    return Integer.compare(o2.getOrdinal(), o1.getOrdinal());
  }
}
