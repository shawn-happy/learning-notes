package com.shawn.study.algorithms.datastructure.linkedlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DoublyLinkedListTest {
  private static final int LOOPS = 10000;
  private static final int TEST_SZ = 40;
  private static final int NUM_NULLS = TEST_SZ / 5;
  private static final int MAX_RAND_NUM = 250;

  DoublyLinkedList<Integer> list;

  @Before
  public void setup() {
    list = new DoublyLinkedList<>();
  }

  @Test
  public void testEmptyList() {
    assertTrue(list.isEmpty());
    assertEquals(list.size(), 0);
  }

  @Test(expected = Exception.class)
  public void testRemoveFirstOfEmpty() {
    list.removeFirst();
  }

  @Test(expected = Exception.class)
  public void testRemoveLastOfEmpty() {
    list.removeLast();
  }

  @Test
  public void testPeekFirstOfEmpty() {
    assertNull(list.peekFirst());
  }

  @Test
  public void testPeekLastOfEmpty() {
    assertNull(list.peekLast());
  }

  @Test
  public void testAddFirst() {
    list.addFirst(3);
    assertEquals(list.size(), 1);
    list.addFirst(5);
    assertEquals(list.size(), 2);
  }

  @Test
  public void testAddLast() {
    list.addLast(3);
    assertEquals(list.size(), 1);
    list.addLast(5);
    assertEquals(list.size(), 2);
  }

  @Test
  public void testAddAt() throws Exception {
    list.addAt(0, 1);
    assertEquals(list.size(), 1);
    list.addAt(1, 2);
    assertEquals(list.size(), 2);
    list.addAt(1, 3);
    assertEquals(list.size(), 3);
    list.addAt(2, 4);
    assertEquals(list.size(), 4);
    list.addAt(1, 8);
    assertEquals(list.size(), 5);
  }

  @Test
  public void testRemoveFirst() {
    list.addFirst(3);
    assertEquals(list.removeFirst(), new Integer(3));
    assertTrue(list.isEmpty());
  }

  @Test
  public void testRemoveLast() {
    list.addLast(4);
    assertEquals(list.removeLast(), new Integer(4));
    assertTrue(list.isEmpty());
  }

  @Test
  public void testPeekFirst() {
    list.addFirst(4);
    assertEquals(list.peekFirst(), new Integer(4));
    assertEquals(list.size(), 1);
  }

  @Test
  public void testPeekLast() {
    list.addLast(4);
    assertEquals(list.peekLast(), Integer.valueOf(4));
    assertEquals(list.size(), 1);
  }

  @Test
  public void testPeeking() {
    // 5
    list.addFirst(5);
    assertEquals(list.peekFirst(), Integer.valueOf(5));
    assertEquals(list.peekLast(), Integer.valueOf(5));

    // 6 - 5
    list.addFirst(6);
    assertEquals(list.peekFirst(), Integer.valueOf(6));
    assertEquals(list.peekLast(), Integer.valueOf(5));

    // 7 - 6 - 5
    list.addFirst(7);
    assertEquals(list.peekFirst(), Integer.valueOf(7));
    assertEquals(list.peekLast(), Integer.valueOf(5));

    // 7 - 6 - 5 - 8
    list.addLast(8);
    assertEquals(list.peekFirst(), Integer.valueOf(7));
    assertEquals(list.peekLast(), Integer.valueOf(8));

    // 7 - 6 - 5
    list.removeLast();
    assertEquals(list.peekFirst(), Integer.valueOf(7));
    assertEquals(list.peekLast(), Integer.valueOf(5));

    // 7 - 6
    list.removeLast();
    assertEquals(list.peekFirst(), Integer.valueOf(7));
    assertEquals(list.peekLast(), Integer.valueOf(6));

    // 6
    list.removeFirst();
    assertEquals(list.peekFirst(), Integer.valueOf(6));
    assertEquals(list.peekLast(), Integer.valueOf(6));
  }

  @Test
  public void testRemoving() {
    DoublyLinkedList<String> strs = new DoublyLinkedList<>();
    strs.add("a");
    strs.add("b");
    strs.add("c");
    strs.add("d");
    strs.add("e");
    strs.add("f");
    strs.remove("b");
    strs.remove("a");
    strs.remove("d");
    strs.remove("e");
    strs.remove("c");
    strs.remove("f");
    assertEquals(strs.size(), 0);
  }

  @Test
  public void testRemoveAt() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.removeAt(0);
    list.removeAt(2);
    assertEquals(list.peekFirst(), Integer.valueOf(2));
    assertEquals(list.peekLast(), Integer.valueOf(3));
    list.removeAt(1);
    list.removeAt(0);
    assertEquals(list.size(), 0);
  }

  @Test
  public void testClear() {
    list.add(22);
    list.add(33);
    list.add(44);
    assertEquals(list.size(), 3);
    list.clear();
    assertEquals(list.size(), 0);
    list.add(22);
    list.add(33);
    list.add(44);
    assertEquals(list.size(), 3);
    list.clear();
    assertEquals(list.size(), 0);
  }

  @Test
  public void testRandomizedRemoving() {
    java.util.LinkedList<Integer> javaLinkedList = new java.util.LinkedList<>();
    for (int loops = 0; loops < LOOPS; loops++) {

      list.clear();
      javaLinkedList.clear();

      List<Integer> randNums = genRandList(TEST_SZ);
      for (Integer value : randNums) {
        javaLinkedList.add(value);
        list.add(value);
      }

      Collections.shuffle(randNums);

      for (int i = 0; i < randNums.size(); i++) {

        Integer rm_val = randNums.get(i);
        assertEquals(javaLinkedList.remove(rm_val), list.remove(rm_val));
        assertEquals(javaLinkedList.size(), list.size());

        java.util.Iterator<Integer> iter1 = javaLinkedList.iterator();
        java.util.Iterator<Integer> iter2 = list.iterator();
        while (iter1.hasNext()) assertEquals(iter1.next(), iter2.next());

        iter1 = javaLinkedList.iterator();
        iter2 = list.iterator();
        while (iter1.hasNext()) assertEquals(iter1.next(), iter2.next());
      }

      list.clear();
      javaLinkedList.clear();

      for (Integer value : randNums) {
        javaLinkedList.add(value);
        list.add(value);
      }

      // Try removing elements whether or not they exist
      for (int i = 0; i < randNums.size(); i++) {

        Integer rm_val = (int) (MAX_RAND_NUM * Math.random());
        assertEquals(javaLinkedList.remove(rm_val), list.remove(rm_val));
        assertEquals(javaLinkedList.size(), list.size());

        java.util.Iterator<Integer> iter1 = javaLinkedList.iterator();
        java.util.Iterator<Integer> iter2 = list.iterator();
        while (iter1.hasNext()) assertEquals(iter1.next(), iter2.next());
      }
    }
  }

  @Test
  public void testRandomizedRemoveAt() {
    java.util.LinkedList<Integer> javaLinkedList = new java.util.LinkedList<>();

    for (int loops = 0; loops < LOOPS; loops++) {

      list.clear();
      javaLinkedList.clear();

      List<Integer> randNums = genRandList(TEST_SZ);

      for (Integer value : randNums) {
        javaLinkedList.add(value);
        list.add(value);
      }

      for (int i = 0; i < randNums.size(); i++) {

        int rm_index = (int) (list.size() * Math.random());

        Integer num1 = javaLinkedList.remove(rm_index);
        Integer num2 = list.removeAt(rm_index);
        assertEquals(num1, num2);
        assertEquals(javaLinkedList.size(), list.size());

        java.util.Iterator<Integer> iter1 = javaLinkedList.iterator();
        java.util.Iterator<Integer> iter2 = list.iterator();
        while (iter1.hasNext()) assertEquals(iter1.next(), iter2.next());
      }
    }
  }

  @Test
  public void testRandomizedIndexOf() {
    java.util.LinkedList<Integer> javaLinkedList = new java.util.LinkedList<>();

    for (int loops = 0; loops < LOOPS; loops++) {

      javaLinkedList.clear();
      list.clear();

      List<Integer> randNums = genUniqueRandList(TEST_SZ);

      for (Integer value : randNums) {
        javaLinkedList.add(value);
        list.add(value);
      }

      Collections.shuffle(randNums);

      for (int i = 0; i < randNums.size(); i++) {
        Integer elem = randNums.get(i);
        Integer index1 = javaLinkedList.indexOf(elem);
        Integer index2 = list.indexOf(elem);

        assertEquals(index1, index2);
        assertEquals(javaLinkedList.size(), list.size());

        java.util.Iterator<Integer> iter1 = javaLinkedList.iterator();
        java.util.Iterator<Integer> iter2 = list.iterator();
        while (iter1.hasNext()) assertEquals(iter1.next(), iter2.next());
      }
    }
  }

  @Test
  public void testToString() {
    DoublyLinkedList<String> strs = new DoublyLinkedList<>();
    assertEquals(strs.toString(), "[  ]");
    strs.add("a");
    assertEquals(strs.toString(), "[ a ]");
    strs.add("b");
    assertEquals(strs.toString(), "[ a, b ]");
    strs.add("c");
    strs.add("d");
    strs.add("e");
    strs.add("f");
    assertEquals(strs.toString(), "[ a, b, c, d, e, f ]");
  }

  @Test
  public void testIndex() {
    DoublyLinkedList<String> strs = new DoublyLinkedList<>();
    strs.add("a");
    strs.add("b");
    strs.add("c");
    strs.add("d");
    strs.add("e");
    strs.add("f");
    assertEquals(6, strs.size());
    assertEquals(1, strs.indexOf("b"));
    assertEquals(1, strs.lastIndexOf("b"));
    assertEquals(strs.indexOf("b"), strs.lastIndexOf("b"));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGet() {
    DoublyLinkedList<String> strs = new DoublyLinkedList<>();
    strs.add("a");
    strs.add("b");
    strs.add("c");
    strs.add("d");
    strs.add("e");
    strs.add("f");
    assertEquals("c", strs.get(2));
    assertEquals("a", strs.get(0));
    assertEquals("e", strs.get(4));
    assertEquals("f", strs.get(5));
    strs.get(6);
  }

  @Test
  public void testAddBeforeOrAfter(){
    DoublyLinkedList<String> strs = new DoublyLinkedList<>();
    strs.add("a");
    strs.add("b");
    strs.addAfter("b", "c");
    strs.add("e");
    strs.addBefore("e","d");
    strs.addAfter("e","f");
    assertEquals(strs.toString(), "[ a, b, c, d, e, f ]");
  }

  // Generate a list of random numbers
  static List<Integer> genRandList(int sz) {
    List<Integer> lst = new ArrayList<>(sz);
    for (int i = 0; i < sz; i++) lst.add((int) (Math.random() * MAX_RAND_NUM));
    for (int i = 0; i < NUM_NULLS; i++) lst.add(null);
    Collections.shuffle(lst);
    return lst;
  }

  // Generate a list of unique random numbers
  static List<Integer> genUniqueRandList(int sz) {
    List<Integer> lst = new ArrayList<>(sz);
    for (int i = 0; i < sz; i++) lst.add(i);
    for (int i = 0; i < NUM_NULLS; i++) lst.add(null);
    Collections.shuffle(lst);
    return lst;
  }
}
