package com.shawn.study.java.cache.impl.jedis;

import static redis.clients.jedis.params.SetParams.setParams;

import com.shawn.study.java.cache.impl.base.AbstractCache;
import com.shawn.study.java.cache.impl.base.ExpireEntry;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import redis.clients.jedis.Jedis;

public class JedisCache<K, V> extends AbstractCache<K, V> {

  private final Jedis jedis;

  private final byte[] keyPrefixBytes;

  private final int keyPrefixBytesLength;

  protected JedisCache(
      CacheManager cacheManager, String cacheName, Configuration<K, V> configuration, Jedis jedis) {
    super(cacheManager, cacheName, configuration);
    this.jedis = jedis;
    this.keyPrefixBytes = buildKeyPrefixBytes(cacheName);
    this.keyPrefixBytesLength = keyPrefixBytes.length;
  }

  @Override
  protected boolean containsEntry(Object key) throws CacheException, ClassCastException {
    final byte[] keyBytes = getKeyBytes(key);
    return jedis.exists(keyBytes);
  }

  @Override
  protected ExpireEntry<K, V> getEntry(Object key) throws CacheException, ClassCastException {
    final byte[] keyBytes = getKeyBytes(key);
    return getEntry(keyBytes);
  }

  @SuppressWarnings("unchecked")
  protected ExpireEntry<K, V> getEntry(byte[] keyBytes) throws CacheException, ClassCastException {
    byte[] valueBytes = jedis.get(keyBytes);
    return deserialize(valueBytes, ExpireEntry.class);
  }

  @Override
  protected void putEntry(ExpireEntry<K, V> entry) throws CacheException, ClassCastException {
    byte[] keyBytes = getKeyBytes(entry.getKey());
    byte[] valueBytes = serialize(entry);
    if (entry.isEternal()) {
      jedis.set(keyBytes, valueBytes);
    } else {
      jedis.set(keyBytes, valueBytes, setParams().px(entry.getExpiredTime()));
    }
  }

  @Override
  protected ExpireEntry<K, V> removeEntry(Object key) throws CacheException, ClassCastException {
    byte[] keyBytes = getKeyBytes(key);
    ExpireEntry<K, V> oldEntry = getEntry(keyBytes);
    jedis.del(keyBytes);
    return oldEntry;
  }

  @Override
  protected void clearEntries() throws CacheException {
    Set<byte[]> keysBytes = jedis.keys(keyPrefixBytes);
    for (byte[] keyBytes : keysBytes) {
      jedis.del(keyBytes);
    }
  }

  @Override
  protected Set<K> keySet() {
    Set<byte[]> keysBytes = jedis.keys(keyPrefixBytes);
    Set<K> keys = new LinkedHashSet<>(keysBytes.size());
    for (byte[] keyBytes : keysBytes) {
      keys.add(deserialize(keyBytes, getConfiguration().getKeyType()));
    }
    return Collections.unmodifiableSet(keys);
  }

  private byte[] buildKeyPrefixBytes(String cacheName) {
    String keyPrefix = "JedisCache-" + cacheName + ":";
    return keyPrefix.getBytes(StandardCharsets.UTF_8);
  }

  private byte[] getKeyBytes(Object key) {
    byte[] suffixBytes = serialize(key);
    int suffixBytesLength = suffixBytes.length;
    byte[] bytes = new byte[keyPrefixBytesLength + suffixBytesLength];
    System.arraycopy(keyPrefixBytes, 0, bytes, 0, keyPrefixBytesLength);
    System.arraycopy(suffixBytes, 0, bytes, keyPrefixBytesLength, suffixBytesLength);
    return bytes;
  }

  private byte[] serialize(Object key) {
    byte[] bytes = null;
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
      // Key -> byte[]
      objectOutputStream.writeObject(key);
      bytes = outputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return bytes;
  }

  public <T> T deserialize(byte[] bytes, Class<T> deserializedType) {
    if (bytes == null) {
      return null;
    }
    Object value = null;
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
      // byte[] -> Value
      value = objectInputStream.readObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return deserializedType.cast(value);
  }
}
