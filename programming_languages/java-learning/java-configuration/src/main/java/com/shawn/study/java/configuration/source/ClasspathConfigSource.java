package com.shawn.study.java.configuration.source;

import java.io.InputStream;

public abstract class ClasspathConfigSource extends BaseConfigSource {

  private String resourceName;

  protected ClasspathConfigSource(String resourceName, String name, int ordinal) {
    super(name, ordinal);
    this.resourceName = resourceName;
    InputStream classpathFile = getClasspathFile();
    load(classpathFile);
    close(classpathFile);
  }

  protected InputStream getClasspathFile() {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
  }

  protected abstract void load(InputStream classpathFile);

  private void close(AutoCloseable autoCloseable) {
    if (autoCloseable != null) {
      try {
        autoCloseable.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
