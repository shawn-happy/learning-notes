package com.shawn.study.deep.in.java.configuration.provider;

import java.util.LinkedHashSet;
import java.util.Set;
import org.eclipse.microprofile.config.Config;

public class DefaultConfigRegistry implements ConfigRegistry {

  private final Configs configs = new Configs();

  @Override
  public void addConfig(Config config) {
    configs.add(new ConfigClassLoaderPair(config, config.getClass().getClassLoader()));
  }

  @Override
  public void addConfig(Config config, ClassLoader classLoader) {
    configs.add(new ConfigClassLoaderPair(config, classLoader));
  }

  @Override
  public void removeIfExists(Config config) {
    configs.remove(new ConfigClassLoaderPair(config, config.getClass().getClassLoader()));
  }

  private static class Configs {
    private final Set<ConfigClassLoaderPair> configs = new LinkedHashSet<>();

    public void add(ConfigClassLoaderPair configClassLoaderPair) {
      configs.add(configClassLoaderPair);
    }

    public void remove(ConfigClassLoaderPair configClassLoaderPair) {
      configs.remove(configClassLoaderPair);
    }
  }

  private static class ConfigClassLoaderPair {
    private final Config config;
    private final ClassLoader classLoader;

    public ConfigClassLoaderPair(Config config, ClassLoader classLoader) {
      this.config = config;
      this.classLoader = classLoader;
    }

    public Config getConfig() {
      return config;
    }

    public ClassLoader getClassLoader() {
      return classLoader;
    }
  }
}
