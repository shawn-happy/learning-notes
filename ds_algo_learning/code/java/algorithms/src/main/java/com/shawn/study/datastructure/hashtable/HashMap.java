package com.shawn.study.datastructure.hashtable;

import java.util.ArrayList;
import java.util.List;

public class HashMap<K, V> implements Map<K, V> {

  // 默认的初始化大小
  private static final int DEFAULT_CAPACITY = 1 << 4;

  // 默认的装载因子
  private static final float DEFAULT_LOAD_FACTOR = 0.75f;

  private Node<K, V>[] table;

  // 实际元素大小
  private int size;

  private int use;

  private int capacity;

  private float loadFactor;

  public HashMap() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  public HashMap(int capacity, float loadFactor) {
    if (capacity < 0) {
      throw new IllegalArgumentException("Illegal capacity: " + capacity);
    }
    if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
      throw new IllegalArgumentException("Illegal loadFactor: " + loadFactor);
    }
    this.capacity = capacity;
    this.loadFactor = loadFactor;
    table = new Node[capacity];
  }

  @Override
  public V put(K k, V v) {
    V oldValue = null;
    // 判断是否需要扩容?
    // 扩容完毕 re肯定需要重新散列
    if (use >= capacity * loadFactor) {
      resize();
    }
    // 得到HASH值,计算出数组的索引
    int index = hash(k);
    if (table[index] == null) { // 判断当前索引位置有没有元素,如果没有直接插入
      table[index] = new Node<>(k, v, null);
    } else { // 如果有元素就需要遍历链表
      // 遍历链表
      Node<K, V> entry = table[index];
      Node<K, V> e = entry;
      while (e != null) {
        if (k == e.getKey() || k.equals(e.getKey())) {
          oldValue = e.value;
          e.value = v;
          return oldValue;
        }
        e = e.next;
      }
      table[index] = new Node<>(k, v, entry);
    }
    ++use;
    ++size;
    return oldValue;
  }

  private void resize() {
    capacity = capacity << 1;
    Node<K, V>[] newTable = new Node[capacity];
    use = 0;
    size = 0;
    rehash(newTable);
  }

  private void rehash(Node<K, V>[] newTable) {
    // 得到原来老的Entry集合 注意遍历单链表
    List<Node<K, V>> entryList = new ArrayList<Node<K, V>>();
    for (Node<K, V> entry : table) {
      if (entry != null) {
        do {
          entryList.add(entry);
          entry = entry.next;
        } while (entry != null);
      }
    }

    // 覆盖旧的引用
    if (newTable.length > 0) {
      table = newTable;
    }

    for (Node<K, V> entry : entryList) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public V get(K k) {
    int index = hash(k);
    if (table[index] == null) {
      return null;
    } else {
      Entry<K, V> entry = table[index];
      do {
        if (k == entry.getKey() || k.equals(entry.getKey())) {
          return entry.getValue();
        }
        entry = entry.next();
      } while (entry != null);
    }
    return null;
  }

  @Override
  public Entry[] entry() {
    Node<K, V>[] tmp = new Node[size];
    int j = 0;
    for (int i = 0; i < capacity; i++) {
      if (table[i] != null) {
        tmp[j++] = table[i];
        if(j == size){
          break;
        }
      }
    }
    return tmp;
  }

  private static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  }

  static class Node<K, V> implements Entry<K, V> {

    K key;

    V value;

    Node<K, V> next;

    Node(K key, V value, Node<K, V> next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public Entry<K, V> next() {
      return next;
    }
  }
}
