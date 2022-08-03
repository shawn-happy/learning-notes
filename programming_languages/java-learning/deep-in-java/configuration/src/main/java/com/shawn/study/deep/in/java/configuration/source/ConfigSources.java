package com.shawn.study.deep.in.java.configuration.source;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.eclipse.microprofile.config.spi.ConfigSource;

public interface ConfigSources extends Iterable<ConfigSource> {

  default Stream<ConfigSource> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  ConfigSource getConfigSource(String name);
}
