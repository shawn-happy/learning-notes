package com.shawn.study.deep.in.java.configuration.source;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.eclipse.microprofile.config.spi.ConfigSource;

public interface ConfigSources extends Iterable<ConfigSource> {

  default Stream<ConfigSource> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  ConfigSource getConfigSource(String name);

  void sorted(Comparator<ConfigSource> comparator);

  void sorted();

  List<ConfigSource> getConfigSources();
}
