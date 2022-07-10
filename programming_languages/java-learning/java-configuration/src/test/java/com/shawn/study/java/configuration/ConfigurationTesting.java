package com.shawn.study.java.configuration;

import static org.junit.Assert.assertEquals;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.junit.Test;

public class ConfigurationTesting {

  @Test
  public void test() {
    ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
    ConfigBuilder builder = configProviderResolver.getBuilder();
    // add default config source
    builder.addDefaultSources();
    // add discovered config source
    builder.addDiscoveredSources();
    // add discovered config value converter
    builder.addDiscoveredConverters();
    // extend config source
    builder.withSources();
    Config config = builder.build();
    configProviderResolver.registerConfig(config, Thread.currentThread().getContextClassLoader());
    config = configProviderResolver.getConfig();
    String value = config.getValue("application.name", String.class);
    assertEquals("In Memory Config", value);
  }
}
