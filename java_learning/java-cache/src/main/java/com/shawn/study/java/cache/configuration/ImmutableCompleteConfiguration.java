package com.shawn.study.java.cache.configuration;

import static com.shawn.study.java.cache.util.ConfigurationUtils.mutableConfiguration;

import java.util.Objects;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.Factory;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheWriter;

public class ImmutableCompleteConfiguration<K, V> implements CompleteConfiguration<K, V> {

  private final CompleteConfiguration<K, V> configuration;

  public ImmutableCompleteConfiguration(Configuration configuration) {
    this.configuration = mutableConfiguration(configuration);
  }

  @Override
  public boolean isReadThrough() {
    return configuration.isReadThrough();
  }

  @Override
  public boolean isWriteThrough() {
    return configuration.isWriteThrough();
  }

  @Override
  public boolean isStatisticsEnabled() {
    return configuration.isStatisticsEnabled();
  }

  @Override
  public boolean isManagementEnabled() {
    return configuration.isManagementEnabled();
  }

  @Override
  public Iterable<CacheEntryListenerConfiguration<K, V>> getCacheEntryListenerConfigurations() {
    return configuration.getCacheEntryListenerConfigurations();
  }

  @Override
  public Factory<CacheLoader<K, V>> getCacheLoaderFactory() {
    return configuration.getCacheLoaderFactory();
  }

  @Override
  public Factory<CacheWriter<? super K, ? super V>> getCacheWriterFactory() {
    return configuration.getCacheWriterFactory();
  }

  @Override
  public Factory<ExpiryPolicy> getExpiryPolicyFactory() {
    return configuration.getExpiryPolicyFactory();
  }

  @Override
  public Class<K> getKeyType() {
    return configuration.getKeyType();
  }

  @Override
  public Class<V> getValueType() {
    return configuration.getValueType();
  }

  @Override
  public boolean isStoreByValue() {
    return configuration.isStoreByValue();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof Configuration)) return false;
    Configuration<?, ?> that = (Configuration<?, ?>) o;
    return Objects.equals(configuration, new ImmutableCompleteConfiguration(that).configuration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configuration);
  }
}
