package io.github.shawn.datastructure.array;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;

/**
 * 动态数组的单元测试类 {@link DynamicArray}
 *
 * @author shawn
 * @since 2020/9/30
 */
public class DynamicArrayTest {

  @Test
  public void testEmptyList() {
    DynamicArray<Integer> list = new DynamicArray<>();
    assertTrue(list.isEmpty());
  }

  @Test(expected = Exception.class)
  public void testRemovingEmpty() {
    DynamicArray<Integer> list = new DynamicArray<>();
    list.remove(0);
  }

  @Test
  public void testRemove() {
    DynamicArray<Integer> list = new DynamicArray<>();
    list.add(1);
    int remove = list.remove(0);
    assertEquals(1, remove);
  }

  @Test
  public void testRemoveByValue() {
    DynamicArray<Integer> list = new DynamicArray<>();
    list.add(1);
    list.add(2);
    list.add(3);
    boolean remove = list.remove(Integer.valueOf(2));
    assertTrue(remove);
    assertEquals(2, list.size());
  }

  @Test(expected = Exception.class)
  public void testIndexOutOfBounds() {
    DynamicArray<Integer> list = new DynamicArray<>();
    list.add(-56);
    list.add(-53);
    list.add(-55);
    list.remove(3);
  }

  @Test(expected = Exception.class)
  public void testIndexOutOfBounds2() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 1000; i++) {
      list.add(i + 1);
    }
    list.remove(1000);
  }

  @Test(expected = Exception.class)
  public void testIndexOutOfBounds3() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 1000; i++) {
      list.add(i + 1);
    }
    list.remove(-1);
  }

  @Test(expected = Exception.class)
  public void testIndexOutOfBounds4() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 1000; i++) {
      list.add(i + 1);
    }
    list.remove(-66);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIndexOutOfBounds5() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 1000; i++) {
      list.add(i + 1);
    }
    list.set(-1, 3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIndexOutOfBounds6() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    list.set(10, 3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIndexOutOfBounds7() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    list.set(15, 3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIndexOutOfBounds8() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    list.get(-2);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIndexOutOfBounds9() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    list.get(10);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testIndexOutOfBounds10() {
    DynamicArray<Integer> list = new DynamicArray<>();
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    list.get(15);
  }

  @Test
  public void testRemoving() {
    DynamicArray<String> list = new DynamicArray<>();
    String[] strs = {"a", "b", "c", "d", "e", null, "g", "h"};
    for (String s : strs) {
      list.add(s);
    }
    boolean ret = list.remove("c");
    assertTrue(ret);

    ret = list.remove("c");
    assertFalse(ret);

    ret = list.remove("h");
    assertTrue(ret);

    ret = list.remove(null);
    assertTrue(ret);

    ret = list.remove("a");
    assertTrue(ret);

    ret = list.remove("a");
    assertFalse(ret);

    ret = list.remove("h");
    assertFalse(ret);

    ret = list.remove(null);
    assertFalse(ret);
  }

  @Test
  public void testRemoving2() {
    DynamicArray<String> list = new DynamicArray<>();
    String[] strs = {"a", "b", "c", "d"};
    for (String s : strs) {
      list.add(s);
    }

    assertTrue(list.remove("a"));
    assertTrue(list.remove("b"));
    assertTrue(list.remove("c"));
    assertTrue(list.remove("d"));

    assertFalse(list.remove("a"));
    assertFalse(list.remove("b"));
    assertFalse(list.remove("c"));
    assertFalse(list.remove("d"));
  }

  @Test
  public void testIndexOfNullElement() {
    DynamicArray<String> list = new DynamicArray<>();
    String[] strs = {"a", "b", null, "d"};
    for (String s : strs) {
      list.add(s);
    }
    assertEquals(2, list.indexOf(null));
  }

  @Test
  public void testAddingElements() {
    DynamicArray<Integer> list = new DynamicArray<>();
    int[] elems = {1, 2, 3, 4, 5, 6, 7};
    for (int elem : elems) {
      list.add(elem);
    }
    for (int i = 0; i < elems.length; i++) {
      assertEquals(list.get(i).intValue(), elems[i]);
    }
  }

  @Test
  public void testAddAndRemove() {
    DynamicArray<Long> list = new DynamicArray<>(0);
    for (int i = 0; i < 55; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 55; i++) {
      list.remove(44L);
    }
    assertTrue(list.isEmpty());

    for (int i = 0; i < 55; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 55; i++) {
      list.remove(0);
    }
    assertTrue(list.isEmpty());

    for (int i = 0; i < 155; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 155; i++) {
      list.remove(44L);
    }
    assertTrue(list.isEmpty());

    for (int i = 0; i < 155; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 155; i++) {
      list.remove(0);
    }
    assertTrue(list.isEmpty());
  }

  @Test
  public void testAddSetRemove() {
    DynamicArray<Long> list = new DynamicArray<>(0);
    for (int i = 0; i < 55; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 55; i++) {
      list.set(i, 33L);
    }
    for (int i = 0; i < 55; i++) {
      list.remove(33L);
    }
    assertTrue(list.isEmpty());

    for (int i = 0; i < 55; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 55; i++) {
      list.set(i, 33L);
    }
    for (int i = 0; i < 55; i++) {
      list.remove(0);
    }
    assertTrue(list.isEmpty());

    for (int i = 0; i < 155; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 155; i++) {
      list.set(i, 33L);
    }
    for (int i = 0; i < 155; i++) {
      list.remove(33L);
    }
    assertTrue(list.isEmpty());

    for (int i = 0; i < 155; i++) {
      list.add(44L);
    }
    for (int i = 0; i < 155; i++) {
      list.remove(0);
    }
    assertTrue(list.isEmpty());
  }

  @Test
  public void testSize() {
    DynamicArray<Integer> list = new DynamicArray<>();
    Integer[] elems = {-76, 45, 66, 3, null, 54, 33};
    for (int i = 0, sz = 1; i < elems.length; i++, sz++) {
      list.add(elems[i]);
      assertEquals(list.size(), sz);
    }
  }

  @Test
  public void testAddIndex1() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 10; i++) {
      list.add(0, i + 1);
    }
    System.out.println(list);
  }

  @Test
  public void testAddIndex2() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    System.out.println(list);
    list.add(2, 22);
    System.out.println(list);
  }

  @Test
  public void testAddIndex3() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    System.out.println(list);
    list.add(list.size(), 22);
    System.out.println(list);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddIndexWithIndexOutOfBounds() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    System.out.println(list);
    list.add(list.size() + 1, 22);
    System.out.println(list);
  }

  @Test
  public void testContains() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 10; i++) {
      list.add(i + 1);
    }
    assertTrue(list.contains(1));
    assertTrue(list.contains(2));
    assertTrue(list.contains(3));
    assertTrue(list.contains(10));

    assertFalse(list.contains(-1));
    assertFalse(list.contains(11));
    assertFalse(list.contains(12));
    assertFalse(list.contains(0));
  }

  @Test
  public void testClear() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 1000; i++) {
      list.add(i + 1);
    }
    assertFalse(list.isEmpty());
    list.clear();
    assertTrue(list.isEmpty());
  }

  @Test
  public void testIterator() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 20; i++) {
      list.add(i + 1);
    }
    for (Integer next : list) {
      System.out.print(next + " ");
    }
    System.out.println();
  }

  @Test(expected = Exception.class)
  public void testIteratorRemove() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    for (int i = 0; i < 20; i++) {
      list.add(i + 1);
    }
    Iterator<Integer> iterator = list.iterator();
    while (iterator.hasNext()) {
      iterator.remove();
    }
  }

  @Test
  public void testListIsEmpty() {
    DynamicArray<Integer> list = new DynamicArray<>(10);
    Assert.assertTrue(list.isEmpty());
    Assert.assertEquals(0, list.size());
    Assert.assertEquals("[]", list.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCapacityLessThanZeroThrowException() {
    new DynamicArray<>(-1);
  }
}
