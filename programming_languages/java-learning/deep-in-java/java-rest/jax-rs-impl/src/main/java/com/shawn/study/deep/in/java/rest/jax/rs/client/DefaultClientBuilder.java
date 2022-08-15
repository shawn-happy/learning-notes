package com.shawn.study.deep.in.java.rest.jax.rs.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Configuration;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

public class DefaultClientBuilder extends ClientBuilder {

  private final ClientConfig config;

  public DefaultClientBuilder() {
    this.config = new ClientConfig();
  }

  @Override
  public ClientBuilder withConfig(Configuration config) {
    this.config.loadFrom(config);
    return this;
  }

  @Override
  public ClientBuilder sslContext(SSLContext sslContext) {
    return this;
  }

  @Override
  public ClientBuilder keyStore(KeyStore keyStore, char[] password) {
    return this;
  }

  @Override
  public ClientBuilder trustStore(KeyStore trustStore) {
    return this;
  }

  @Override
  public ClientBuilder hostnameVerifier(HostnameVerifier verifier) {
    return this;
  }

  @Override
  public ClientBuilder executorService(ExecutorService executorService) {
    return this;
  }

  @Override
  public ClientBuilder scheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
    return this;
  }

  @Override
  public ClientBuilder connectTimeout(long timeout, TimeUnit unit) {
    return this;
  }

  @Override
  public ClientBuilder readTimeout(long timeout, TimeUnit unit) {
    return this;
  }

  @Override
  public Client build() {
    return new DefaultClient();
  }

  @Override
  public Configuration getConfiguration() {
    return this.config;
  }

  @Override
  public ClientBuilder property(String name, Object value) {
    return this;
  }

  @Override
  public ClientBuilder register(Class<?> componentClass) {
    return this;
  }

  @Override
  public ClientBuilder register(Class<?> componentClass, int priority) {
    return this;
  }

  @Override
  public ClientBuilder register(Class<?> componentClass, Class<?>... contracts) {
    return this;
  }

  @Override
  public ClientBuilder register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
    return this;
  }

  @Override
  public ClientBuilder register(Object component) {
    return this;
  }

  @Override
  public ClientBuilder register(Object component, int priority) {
    return this;
  }

  @Override
  public ClientBuilder register(Object component, Class<?>... contracts) {
    return this;
  }

  @Override
  public ClientBuilder register(Object component, Map<Class<?>, Integer> contracts) {
    return this;
  }
}
