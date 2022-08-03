package com.shawn.study.deep.in.java.configuration.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class MutableConfigSources implements ConfigSources {

  private final List<ConfigSource> configSources = new ArrayList<>();

  public void addFirst(ConfigSource configSource) {}

  public void addLast(ConfigSource configSource) {}

  public void addBefore(String name, ConfigSource configSource) {}

  public void addAfter(String name, ConfigSource configSource) {}

  private void addAtIndex(int index, ConfigSource configSource) {}

  protected void removeIfPresent(ConfigSource configSource) {}

  @Override
  public ConfigSource getConfigSource(String name) {
    return this.configSources
        .stream()
        .sorted(ConfigSourceOrdinalComparator.INSTANCE)
        .filter(configSource -> StringUtils.equals(name, configSource.getName()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Iterator<ConfigSource> iterator() {
    return this.configSources.iterator();
  }

  @Override
  public Spliterator<ConfigSource> spliterator() {
    return Spliterators.spliterator(this.configSources, 0);
  }

  @Override
  public Stream<ConfigSource> stream() {
    return this.configSources.stream();
  }
}
