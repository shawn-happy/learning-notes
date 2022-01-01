package com.shawn.study.deep.in.java.collection.implementations;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class ConvenienceImplementationsDemo {

  // singleton collection implementation
  @Test
  public void createSingletonCollection() {
    List<String> singletonList = Collections.singletonList("Singleton List");
    Set<String> singletonSet = Collections.singleton("Singleton Set");
    Map<String, String> singletonMap = Collections.singletonMap("key", "Singleton Map");
    Assert.assertEquals(1, singletonList.size());
    Assert.assertEquals(1, singletonSet.size());
    Assert.assertEquals(1, singletonMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void createSingletonCollectionAndAddOtherElementThrowException() {
    List<String> singletonList = Collections.singletonList("Singleton List");
    singletonList.add("other element");
    Assert.assertEquals(2, singletonList.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void createSingletonCollectionAndSetElementThrowException() {
    List<String> singletonList = Collections.singletonList("Singleton List");
    Assert.assertEquals("Singleton List", singletonList.get(0));
    singletonList.set(0, "Hello Singleton List");
    Assert.assertEquals(1, singletonList.size());
    Assert.assertEquals("Hello Singleton List", singletonList.get(0));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void createSingletonCollectionAndRemoveElementThrowException() {
    List<String> singletonList = Collections.singletonList("Singleton List");
    Assert.assertEquals("Singleton List", singletonList.get(0));
    singletonList.remove(0);
    Assert.assertEquals(0, singletonList.size());
  }

  // create empty collection
  private static void createEmptyCollection() {
    // create empty enumeration
    Enumeration<Object> emptyEnumeration = Collections.emptyEnumeration();

    // create empty list
    List<Object> emptyList = Collections.emptyList();
    List emptyList1 = Collections.EMPTY_LIST;

    // create empty set
    Set emptySet = Collections.EMPTY_SET;
    Set<Object> emptySet1 = Collections.emptySet();
    SortedSet<Object> emptySortedSet = Collections.emptySortedSet();
    NavigableSet<Object> emptyNavigableSet = Collections.emptyNavigableSet();

    // create empty map
    Map emptyMap = Collections.EMPTY_MAP;
    Map<Object, Object> emptyMap1 = Collections.emptyMap();
    SortedMap<Object, Object> emptySortedMap = Collections.emptySortedMap();
    NavigableMap<Object, Object> emptyNavigableMap = Collections.emptyNavigableMap();

    // create empty iterator
    Iterator<Object> emptyIterator = Collections.emptyIterator();
    ListIterator<Object> emptyListIterator = Collections.emptyListIterator();
  }

  // conversion each other
  @Test
  public void conversion() {
    // array to list
    String[] strings = new String[] {"Java", "Spring", "Web", "Tomcat"};
    List<String> stringList = Arrays.asList(strings);
    List<String> stringList1 = Arrays.stream(strings).collect(Collectors.toList());
    List<String> stringList2 = List.of(strings);
    Assert.assertEquals(4, stringList.size());
    Assert.assertEquals(4, stringList1.size());
    Assert.assertEquals(4, stringList2.size());

    // map to set
    Map<String, Boolean> map = new HashMap<>();
    Set<String> set = Collections.newSetFromMap(map);
    set.add("Java");
    set.add("Spring");
    set.add("Web");
    set.add("Tomcat");
    Assert.assertEquals(4, set.size());
    Assert.assertTrue(set.contains("Java"));

    // enumeration to list
    Vector<String> vector = new Vector<>();
    vector.add("Java");
    vector.add("Spring");
    vector.add("Web");
    vector.add("Tomcat");
    Enumeration<String> elements = vector.elements();
    ArrayList<String> list = Collections.list(elements);
    Assert.assertEquals(4, list.size());
    Assert.assertTrue(list.contains("Java"));

    int hashCode = Arrays.hashCode(strings);
    System.out.println(hashCode);

    String s = Arrays.toString(strings);
    System.out.println(s);
  }
}
