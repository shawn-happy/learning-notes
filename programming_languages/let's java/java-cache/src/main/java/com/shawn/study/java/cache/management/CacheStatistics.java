package com.shawn.study.java.cache.management;

import javax.cache.management.CacheStatisticsMXBean;

public interface CacheStatistics extends CacheStatisticsMXBean {

  CacheStatistics reset();

  CacheStatistics cacheHits();

  CacheStatistics cacheGets();

  CacheStatistics cachePuts();

  CacheStatistics cacheRemovals();

  CacheStatistics cacheEvictions();

  CacheStatistics cacheGetsTime(long costTime);

  CacheStatistics cachePutsTime(long costTime);

  CacheStatistics cacheRemovesTime(long costTime);
}
