package io.github.deep.in.java.microprofile.rest.application;

import io.github.deep.in.java.microprofile.rest.resource.MyResource;
import jakarta.ws.rs.core.Application;
import java.util.Set;

public class MyApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return Set.of(MyResource.class);
  }
}
