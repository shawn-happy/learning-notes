package com.shawn.study.deep.in.java.collection.implementations;

import java.util.*;
import org.junit.Test;

/** 遗留实现 */
public class LegacyImplementationsDemo {

  public void testEnumeration() {
    // Enumeration 主要用于迭代早期实现，由于 java.util.Iterator 从 Java 1.2 引入。
    String value = "1,2,3";

    StringTokenizer tokenizer = new StringTokenizer(value, ",");

    while (((Enumeration<Object>) tokenizer).hasMoreElements()) { // 等价 Iterator.hasNext()
      String element =
          String.valueOf(((Enumeration<Object>) tokenizer).nextElement()); // 等价 Iterator.next();
      System.out.println(element);
    }
  }

  public void testHashTable() {}

  @Test
  public void testBitSet() {
    Random random = new Random();

    List<Integer> list = new ArrayList<>();
    for (int i = 0; i < 10000000; i++) {
      int randomResult = random.nextInt(100000000);
      list.add(randomResult);
    }
    //    System.out.println("产生的随机数有");
    //    for (int i = 0; i < list.size(); i++) {
    //      System.out.println(list.get(i));
    //    }
    BitSet bitSet = new BitSet(100000000);
    for (int i = 0; i < 10000000; i++) {
      bitSet.set(list.get(i));
    }

    System.out.println("0~1亿不在上述随机数中有" + bitSet.cardinality());
    //    for (int i = 0; i < 100000000; i++) {
    //      if (!bitSet.get(i)) {
    //        System.out.println(i);
    //      }
    //    }
  }
}
