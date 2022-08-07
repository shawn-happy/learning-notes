package com.shawn.study.deep.in.java.configuration.provider;

import org.eclipse.microprofile.config.Config;

public interface ConfigRegistry {

  void addConfig(Config config);

  void addConfig(Config config, ClassLoader classLoader);

  void removeIfExists(Config config);
}
