package com.shawn.study.java.configuration.source;

import static java.util.ServiceLoader.load;
import static java.util.stream.Stream.of;

import com.shawn.study.java.configuration.DefaultConfigSourceComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class ConfigSources implements Iterable<ConfigSource> {

  private boolean addedDefaultConfigSources;

  private boolean addedDiscoveredConfigSources;

  private List<ConfigSource> configSources = new ArrayList<>();

  private ClassLoader classLoader;

  public ConfigSources(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  public void setClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  public void addDefaultSources() {
    if (addedDefaultConfigSources) {
      return;
    }
    addConfigSources(
        JavaSystemPropertiesConfigSource.class,
        ClasspathPropertiesConfigSource.class,
        ClasspathYamlConfigSource.class);
    addedDefaultConfigSources = true;
  }

  public void addDiscoveredSources() {
    if (addedDiscoveredConfigSources) {
      return;
    }

    addConfigSources(load(ConfigSource.class, classLoader));
    addedDiscoveredConfigSources = true;
  }

  @SafeVarargs
  public final void addConfigSources(Class<? extends ConfigSource>... configSourceClasses) {
    addConfigSources(of(configSourceClasses).map(this::newInstance).toArray(ConfigSource[]::new));
  }

  public void addConfigSources(ConfigSource... configSources) {
    addConfigSources(Arrays.asList(configSources));
  }

  public void addConfigSources(Iterable<ConfigSource> configSources) {
    configSources.forEach(this.configSources::add);
    this.configSources.sort(DefaultConfigSourceComparator.INSTANCE);
  }

  private ConfigSource newInstance(Class<? extends ConfigSource> configSourceClass) {
    ConfigSource instance = null;
    try {
      instance = configSourceClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    return instance;
  }

  @Override
  public Iterator<ConfigSource> iterator() {
    return configSources.iterator();
  }

  public boolean isAddedDefaultConfigSources() {
    return addedDefaultConfigSources;
  }

  public boolean isAddedDiscoveredConfigSources() {
    return addedDiscoveredConfigSources;
  }

  public ClassLoader getClassLoader() {
    return classLoader;
  }
}
