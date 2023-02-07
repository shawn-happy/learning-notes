package com.shawn.study.deep.in.java.concurrency.sdk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache<K, V> {

  private final Map<K, V> m = new HashMap<>();
  private final ReadWriteLock rwl = new ReentrantReadWriteLock();
  private final Lock r = rwl.readLock();
  private final Lock w = rwl.writeLock();

  // 错误示例
  public final V get(K key) {
    V v = null;
    // 读缓存
    r.lock();
    try {
      v = m.get(key);
      if (v == null) {
        w.lock();
        try {
          // v = 省略代码
          m.put(key, v);
        } finally {
          w.unlock();
        }
      }
    } finally {
      r.unlock();
    }
    return v;
  }
}
