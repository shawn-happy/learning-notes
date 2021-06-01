package com.shawn.study.algorithms.datastructure.hashtable;

import com.shawn.study.algorithms.datastructure.hashtable.HashMap.Node;
import com.shawn.study.algorithms.datastructure.hashtable.Map.Entry;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class HashMapTest {

  private Map<Integer, Integer> map;

  @Before
  public void init(){
    map = new HashMap<>();
  }

  @Test
  public void test(){
    map.put(1,1);
    Integer value = map.get(1);
    System.out.println(value);
    map.put(1, 2);
    value = map.get(1);
    System.out.println(value);

    map.put(2, 3);
    map.put(3, 4);
    map.put(4, 5);
    System.out.println(map.get(2));
    System.out.println(map.get(3));
    System.out.println(map.get(4));

    System.out.println(map.size());

    Entry<Integer, Integer>[] entrySet = map.entry();
    for (Entry<Integer, Integer> node : entrySet){
      System.out.println(node.getKey() + "=" + node.getValue());
    }
  }

}
