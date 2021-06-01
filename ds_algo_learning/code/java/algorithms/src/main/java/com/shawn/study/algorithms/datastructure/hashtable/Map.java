package com.shawn.study.algorithms.datastructure.hashtable;

public interface Map<K, V> {

  V put(K k, V v);

  V get(K k);

  Entry[] entry();

  interface Entry<K, V>{
    K getKey();
    V getValue();

    Entry<K, V> next();
  }

}
