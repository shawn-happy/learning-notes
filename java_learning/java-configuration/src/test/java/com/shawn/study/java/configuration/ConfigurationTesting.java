package com.shawn.study.java.configuration;

import java.util.ServiceLoader;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.junit.Assert;
import org.junit.Test;

public class ConfigurationTesting {

  @Test
  public void test() {
    ServiceLoader<ConfigProviderResolver> serviceLoader =
        ServiceLoader.load(
            ConfigProviderResolver.class, Thread.currentThread().getContextClassLoader());
    for (ConfigProviderResolver next : serviceLoader) {
      Config config = next.getConfig();
      Object value = config.getValue("file.encoding", null);
      Assert.assertEquals("UTF-8", value);
    }
  }
}
