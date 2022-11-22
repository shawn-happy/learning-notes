package com.shawn.study.redis.basic;

import java.util.HashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class RedisDistributeLock {

  private final ThreadLocal<Map<String, Integer>> lockers = new ThreadLocal<>();

  private final Jedis jedis;

  public RedisDistributeLock(Jedis jedis) {
    this.jedis = jedis;
  }

  private boolean _lock(String key) {
    SetParams setParams = new SetParams();
    return jedis.set(key, "", setParams.nx().ex(5)) != null;
  }

  private void _unlock(String key) {
    jedis.del(key);
  }

  private Map<String, Integer> currentLockers() {
    Map<String, Integer> refs = lockers.get();
    if (refs != null) {
      return refs;
    }
    lockers.set(new HashMap<>());
    return lockers.get();
  }

  public boolean lock(String key) {
    Map<String, Integer> refs = currentLockers();
    Integer refCount = refs.get(key);
    if (refCount != null) {
      refs.put(key, refCount + 1);
      return true;
    }
    boolean ok = this._lock(key);
    if (ok) {
      refs.put(key, 1);
    }
    return ok;
  }

  public boolean unlock(String key) {
    Map<String, Integer> refs = currentLockers();
    Integer refCount = refs.get(key);
    if (refCount == null) {
      return false;
    }
    refCount -= 1;
    if (refCount > 0) {
      refs.put(key, refCount);
    } else {
      refs.remove(key);
      this._unlock(key);
    }
    return true;
  }
}
