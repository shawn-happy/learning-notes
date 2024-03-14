package io.github.shawn.datastructure.linkedlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.github.shawn.datastructure.linkedlist.SingleLinkedList;
import java.util.Iterator;
import org.junit.Test;

/**
 * 单向链表测试类 {@link SingleLinkedList}
 *
 * @author shawn
 * @since 2020/10/1
 */
public class SingleLinkedListTest {

  @Test
  public void testAdd() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i);
    }
    assertEquals(10, linkedList.size());
  }

  @Test
  public void testAddByIndex() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    linkedList.add(0);
    for (int i = 0; i < 10; i++) {
      linkedList.add(i, i + 1);
    }
    linkedList.add(2, 33);
    System.out.println(linkedList.toString());
  }

  @Test
  public void testAddLast() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.addLast(i);
    }
    System.out.println(linkedList.toString());
  }

  @Test
  public void testAddFirst() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.addFirst(i);
    }
    System.out.println(linkedList.toString());
  }

  @Test
  public void testAddBefore() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    System.out.printf("init data: %s%n", linkedList.toString());
    linkedList.addBefore(linkedList.get(2), 22);
    System.out.printf("add before index: %d, data: %s%n", 2, linkedList.toString());
    linkedList.addBefore(linkedList.get(0), 0);
    System.out.printf("add before index: %d, data: %s%n", 0, linkedList.toString());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddBeforeException() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    System.out.printf("init data: %s%n", linkedList.toString());
    linkedList.addBefore(linkedList.get(2), 22);
    System.out.printf("add before index: %d, %s%n", 2, linkedList.toString());
    linkedList.addBefore(linkedList.get(linkedList.size()), 0);
    System.out.printf("add before index: %d, %s%n", 0, linkedList.toString());
  }

  @Test
  public void testAddAfter() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    System.out.printf("init data: %s%n", linkedList.toString());
    linkedList.addAfter(linkedList.get(2), 22);
    System.out.printf("add after index: %d, %s%n", 2, linkedList.toString());
    linkedList.addAfter(linkedList.get(0), 0);
    System.out.printf("add after index: %d, %s%n", 0, linkedList.toString());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddAfterException() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    System.out.printf("init data: %s%n", linkedList.toString());
    linkedList.addAfter(linkedList.get(2), 22);
    System.out.printf("add after index: %d, %s%n", 2, linkedList.toString());
    linkedList.addAfter(linkedList.get(linkedList.size()), 0);
    System.out.printf("add after index: %d, %s%n", 0, linkedList.toString());
  }

  @Test
  public void testEmpty() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    assertTrue(linkedList.isEmpty());
    linkedList.add(2);
    assertFalse(linkedList.isEmpty());
  }

  @Test
  public void testContains() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    assertFalse(linkedList.contains(null));
    assertFalse(linkedList.contains(0));
    linkedList.add(0);
    linkedList.add(3);
    linkedList.add(6);
    assertFalse(linkedList.contains(2));
    assertTrue(linkedList.contains(3));
    assertTrue(linkedList.contains(6));
    assertTrue(linkedList.contains(0));
  }

  @Test
  public void testSet() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    System.out.println(linkedList);
    linkedList.set(2, 33);
    linkedList.set(3, 44);
    linkedList.set(6, 77);
    System.out.println(linkedList);
  }

  @Test
  public void testRemoveByIndex() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    Integer left = linkedList.get(2);
    Integer right = linkedList.remove(2);
    assertEquals(left, right);
    System.out.println(linkedList);
    left = linkedList.get(2);
    right = linkedList.remove(2);
    assertEquals(left, right);
    System.out.println(linkedList);
    left = linkedList.get(5);
    right = linkedList.remove(5);
    assertEquals(left, right);
    System.out.println(linkedList);
  }

  @Test
  public void testRemoveByItem() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    assertTrue(linkedList.remove(Integer.valueOf(2)));
    System.out.println(linkedList);
    assertFalse(linkedList.remove(Integer.valueOf(2)));
    System.out.println(linkedList);
    assertTrue(linkedList.remove(Integer.valueOf(5)));
    System.out.println(linkedList);
  }

  @Test
  public void testIterator() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    for (Integer integer : linkedList) {
      System.out.println(integer);
    }
  }

  @Test
  public void testClear() {
    SingleLinkedList<Integer> linkedList = new SingleLinkedList<>();
    for (int i = 0; i < 10; i++) {
      linkedList.add(i + 1);
    }
    for (Integer integer : linkedList) {
      System.out.println(integer);
    }
    linkedList.clear();
    Iterator<Integer> it = linkedList.iterator();
    assertFalse(it.hasNext());
  }
}
