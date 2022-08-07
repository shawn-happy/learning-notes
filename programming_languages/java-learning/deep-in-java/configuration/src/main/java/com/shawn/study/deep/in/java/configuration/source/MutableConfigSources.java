package com.shawn.study.deep.in.java.configuration.source;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class MutableConfigSources implements ConfigSources {

  private final List<ConfigSource> configSources = new ArrayList<>();

  public void addFirst(ConfigSource configSource) {
    removeIfPresent(configSource);
    this.configSources.add(0, configSource);
  }

  public void addLast(ConfigSource configSource) {
    removeIfPresent(configSource);
    this.configSources.add(configSource);
  }

  public void addBefore(String name, ConfigSource configSource) {
    assertLegalRelativeAddition(name, configSource);
    removeIfPresent(configSource);
    int index = assertPresentAndGetIndex(name);
    addAtIndex(index, configSource);
  }

  public void addAfter(String name, ConfigSource configSource) {
    assertLegalRelativeAddition(name, configSource);
    removeIfPresent(configSource);
    int index = assertPresentAndGetIndex(name);
    addAtIndex(index + 1, configSource);
  }

  protected void removeIfPresent(ConfigSource configSource) {
    this.configSources.remove(configSource);
  }

  @Override
  public ConfigSource getConfigSource(String name) {
    sorted();
    return this.configSources
        .stream()
        .sorted(ConfigSourceOrdinalComparator.INSTANCE)
        .filter(configSource -> StringUtils.equals(name, configSource.getName()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void sorted(Comparator<ConfigSource> comparator) {
    this.configSources.sort(comparator);
  }

  @Override
  public void sorted() {
    sorted(ConfigSourceComparator.newComparator());
  }

  @Override
  public List<ConfigSource> getConfigSources() {
    sorted();
    return this.configSources;
  }

  @Override
  public Iterator<ConfigSource> iterator() {
    sorted();
    return this.configSources.iterator();
  }

  @Override
  public Spliterator<ConfigSource> spliterator() {
    sorted();
    return Spliterators.spliterator(this.configSources, 0);
  }

  private void assertLegalRelativeAddition(
      String relativePropertySourceName, ConfigSource configSource) {
    String newPropertySourceName = configSource.getName();
    if (relativePropertySourceName.equals(newPropertySourceName)) {
      throw new IllegalArgumentException(
          "ConfigSource named '" + newPropertySourceName + "' cannot be added relative to itself");
    }
  }

  private int assertPresentAndGetIndex(String name) {
    int index = -1;
    for (int i = 0; i < configSources.size(); i++) {
      ConfigSource configSource = configSources.get(i);
      if (StringUtils.equals(name, configSource.getName())) {
        index = i;
        break;
      }
    }
    if (index == -1) {
      throw new IllegalArgumentException("ConfigSource named '" + name + "' does not exist");
    }
    return index;
  }

  private void addAtIndex(int index, ConfigSource configSource) {
    removeIfPresent(configSource);
    this.configSources.add(index, configSource);
  }
}
