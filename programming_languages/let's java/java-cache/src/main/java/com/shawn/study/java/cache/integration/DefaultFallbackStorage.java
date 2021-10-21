package com.shawn.study.java.cache.integration;

import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

public class DefaultFallbackStorage extends AbstractFallbackStorage<Object, Object> {

  protected DefaultFallbackStorage(int priority) {
    super(priority);
  }

  @Override
  public void destroy() {}

  @Override
  public Object load(Object key) throws CacheLoaderException {
    return null;
  }

  @Override
  public void write(Entry<?, ?> entry) throws CacheWriterException {}

  @Override
  public void delete(Object key) throws CacheWriterException {}
}
