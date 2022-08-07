package com.shawn.study.deep.in.java.configuration;

import com.shawn.study.deep.in.java.configuration.source.PropertiesResourceConfigSource;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.junit.Assert;
import org.junit.Test;

public class Tests {

  @Test
  public void test() {
    ConfigProviderResolver instance = ConfigProviderResolver.instance();
    Config config = instance.getConfig();
    String value = config.getValue("user.dir", String.class);
    Assert.assertEquals(System.getProperties().getProperty("user.dir"), value);
  }

  @Test
  public void testGetApplicationName() {
    ConfigProviderResolver instance = ConfigProviderResolver.instance();
    ConfigBuilder builder = instance.getBuilder();
    ConfigSource configSource = new PropertiesResourceConfigSource();
    builder.withSources(configSource);
    Config config = builder.build();
    ConfigValue configValue = config.getConfigValue("application.name");
    Assert.assertNotNull(configValue);
    String value = configValue.getValue();
    Assert.assertEquals("microprofile.config.properties", value);
  }
}
