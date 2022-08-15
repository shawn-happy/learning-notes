package com.shawn.study.deep.in.java.rest.jax.rs.client;

import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.Configurable;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Feature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientConfig implements Configuration, Configurable<ClientConfig> {

  private final Map<String, Object> properties;
  private final Map<String, Object> immutableProperties;

  private final Set<Class<?>> componentClasses;
  private final Set<Class<?>> immutableComponentClasses;

  private final Set<Object> components;
  private final Set<Object> immutableComponents;

  private final List<ComponentRegistry> componentRegistries;

  public ClientConfig() {
    this.properties = new HashMap<>();
    this.components = new LinkedHashSet<>();
    this.componentClasses = new LinkedHashSet<>();
    this.componentRegistries = new ArrayList<>();

    this.immutableProperties = Collections.unmodifiableMap(this.properties);
    this.immutableComponents = Collections.unmodifiableSet(this.components);
    this.immutableComponentClasses = Collections.unmodifiableSet(this.componentClasses);
  }

  public ClientConfig(
      Map<String, Object> properties, Set<Class<?>> componentClasses, Set<Object> components) {
    this.properties = properties;
    this.components = components;
    this.componentClasses = componentClasses;
    this.componentRegistries = new ArrayList<>();

    this.immutableProperties = Collections.unmodifiableMap(this.properties);
    this.immutableComponents = Collections.unmodifiableSet(this.components);
    this.immutableComponentClasses = Collections.unmodifiableSet(this.componentClasses);
  }

  public ClientConfig(ClientConfig config) {
    this.properties = new HashMap<>();
    this.components = new LinkedHashSet<>();
    this.componentClasses = new LinkedHashSet<>();
    this.componentRegistries = new ArrayList<>();

    this.immutableProperties = Collections.unmodifiableMap(this.properties);
    this.immutableComponents = Collections.unmodifiableSet(this.components);
    this.immutableComponentClasses = Collections.unmodifiableSet(this.componentClasses);

    copy(config, false);
  }

  @Override
  public RuntimeType getRuntimeType() {
    return RuntimeType.CLIENT;
  }

  @Override
  public Map<String, Object> getProperties() {
    return immutableProperties;
  }

  @Override
  public Object getProperty(String name) {
    return immutableProperties.get(name);
  }

  @Override
  public Collection<String> getPropertyNames() {
    return immutableProperties.keySet();
  }

  @Override
  public boolean isEnabled(Feature feature) {
    return isEnabled(feature.getClass());
  }

  @Override
  public boolean isEnabled(Class<? extends Feature> featureClass) {
    return true;
  }

  @Override
  public boolean isRegistered(Object component) {
    return this.immutableComponents.contains(component);
  }

  @Override
  public boolean isRegistered(Class<?> componentClass) {
    return this.immutableComponentClasses.contains(componentClass);
  }

  @Override
  public Map<Class<?>, Integer> getContracts(Class<?> componentClass) {
    return null;
  }

  @Override
  public Set<Class<?>> getClasses() {
    return componentClasses;
  }

  @Override
  public Set<Object> getInstances() {
    return components;
  }

  @Override
  public Configuration getConfiguration() {
    return this;
  }

  @Override
  public ClientConfig property(String name, Object value) {
    this.properties.put(name, value);
    return this;
  }

  @Override
  public ClientConfig register(Class<?> componentClass) {
    checkComponentClassNotNull(componentClass);
    if (componentClasses.add(componentClass)) {
      processComponentRegistry(componentClass, null, -1);
    }
    return this;
  }

  @Override
  public ClientConfig register(Class<?> componentClass, int priority) {
    checkComponentClassNotNull(componentClass);
    if (componentClasses.add(componentClass)) {
      processComponentRegistry(componentClass, null, priority);
    }
    return this;
  }

  @Override
  public ClientConfig register(Class<?> componentClass, Class<?>... contracts) {
    checkComponentClassNotNull(componentClass);
    if (componentClasses.add(componentClass)) {
      processComponentRegistry(componentClass, null, -1);
    }
    return this;
  }

  @Override
  public ClientConfig register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
    checkComponentClassNotNull(componentClass);
    if (componentClasses.add(componentClass)) {
      processComponentRegistry(componentClass, null, -1);
    }
    return this;
  }

  @Override
  public ClientConfig register(Object component) {
    checkComponentNotNull(component);
    if (components.add(component)) {
      processComponentRegistry(null, component, -1);
    }
    return this;
  }

  @Override
  public ClientConfig register(Object component, int priority) {
    checkComponentNotNull(component);
    if (components.add(component)) {
      processComponentRegistry(null, component, priority);
    }
    return this;
  }

  @Override
  public ClientConfig register(Object component, Class<?>... contracts) {
    checkComponentNotNull(component);
    if (components.add(component)) {
      processComponentRegistry(null, component, -1);
    }
    return this;
  }

  @Override
  public ClientConfig register(Object component, Map<Class<?>, Integer> contracts) {
    checkComponentNotNull(component);
    if (components.add(component)) {
      processComponentRegistry(null, component, -1);
    }
    return this;
  }

  private void checkComponentNotNull(final Object component) {
    if (component == null) {
      throw new IllegalArgumentException("Registered component instance cannot be null");
    }
  }

  private void checkComponentClassNotNull(final Class<?> componentClass) {
    if (componentClass == null) {
      throw new IllegalArgumentException("Registered component class cannot be null.");
    }
  }

  private void processComponentRegistry(
      final Class<?> componentClass, final Object component, final int priority) {
    final ComponentRegistry registration =
        (component != null)
            ? new ComponentRegistry(component, priority)
            : new ComponentRegistry(componentClass, priority);
    componentRegistries.add(registration);
  }

  private void copy(ClientConfig config, boolean loadConfig) {
    if (loadConfig) {
      this.componentRegistries.clear();
    }
    this.properties.clear();
    this.components.clear();
    this.componentClasses.clear();
    this.properties.putAll(config.getProperties());
    this.components.addAll(config.getInstances());
    this.componentClasses.addAll(config.getClasses());
    this.componentRegistries.addAll(config.componentRegistries);
  }

  public ClientConfig loadFrom(final Configuration config) {
    if (config instanceof ClientConfig) {
      // If loading from CommonConfig then simply copy properties and check whether given config has
      // been initialized.
      final ClientConfig clientConfig = (ClientConfig) config;
      copy(clientConfig, true);
    } else {
      this.properties.putAll(config.getProperties());
      for (final Class<?> clazz : config.getClasses()) {
        register(clazz, config.getContracts(clazz));
      }

      for (Object instance : config.getInstances()) {
        register(instance, config.getContracts(instance.getClass()));
      }
    }

    return this;
  }
}
