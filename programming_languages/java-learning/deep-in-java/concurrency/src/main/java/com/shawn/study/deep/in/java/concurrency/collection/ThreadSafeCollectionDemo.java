package com.shawn.study.deep.in.java.concurrency.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 创建线程安全的List
 *
 * @author shawn
 */
public class ThreadSafeCollectionDemo {

  public static void main(String[] args) {
    // list的线程安全
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    // 同步包装器
    list = Collections.synchronizedList(list);
    list = new Vector<>(list);
    list = new CopyOnWriteArrayList<>(list);

    Set<Integer> set = new HashSet<>();
    for (int i = 0; i < 5; i++) {
      set.add(i + 1);
    }

    // Set 的线程安全
    // 同步包装器
    set = Collections.synchronizedSet(set);
    set = new CopyOnWriteArraySet(set);

    Map<Integer, Integer> map = new HashMap<>();
    map.put(1, 1);
    // Map的线程安全
    new Hashtable<>(map);

    Collections.synchronizedMap(map);

    new ConcurrentHashMap<>(map);

    // java 9
    // Set.of(), List.of(), Map.of()
  }
}
