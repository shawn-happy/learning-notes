package io.github.shawn.datastructure.hashtable;

import io.github.shawn.datastructure.hashtable.HashMap;
import io.github.shawn.datastructure.hashtable.Map;
import io.github.shawn.datastructure.hashtable.Map.Entry;
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

    Entry<Integer, Integer>[] entrySet = map.entry();
    for (Entry<Integer, Integer> node : entrySet){
      System.out.println(node.getKey() + "=" + node.getValue());
    }
  }

}
