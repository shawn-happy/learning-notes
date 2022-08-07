package com.shawn.study.deep.in.java.configuration.source;

import java.util.Comparator;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class ConfigSourceComparator implements Comparator<ConfigSource> {

  private static final Comparator<ConfigSource> COMPARATOR = new ConfigSourceComparator();

  private ConfigSourceComparator() {}

  public static Comparator<ConfigSource> newComparator() {
    return COMPARATOR;
  }

  @Override
  public int compare(ConfigSource o1, ConfigSource o2) {
    return Integer.compare(o1.getOrdinal(), o2.getOrdinal());
  }
}
