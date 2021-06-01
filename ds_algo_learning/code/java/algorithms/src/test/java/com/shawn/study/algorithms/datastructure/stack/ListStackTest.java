package com.shawn.study.algorithms.datastructure.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shawn
 * @since 2020/10/13
 */
public class ListStackTest {
  private Stack<Integer> stack;

  @Before
  public void setBase() {
    stack = new ListStack<>();
  }

  @Test
  public void testEmpty() {
    assertTrue(stack.isEmpty());
    stack.push(23);
    assertFalse(stack.isEmpty());
    stack.peek();
    assertFalse(stack.isEmpty());
    stack.pop();
    assertTrue(stack.isEmpty());
  }

  @Test
  public void testSize() {
    assertEquals(0, stack.size());
    stack.push(23);
    assertEquals(1, stack.size());
    stack.peek();
    assertEquals(1, stack.size());
    stack.pop();
    assertEquals(0, stack.size());
  }

  @Test
  public void testPush() {
    for (int i = 0; i < 30; i++) {
      stack.push(i);
    }
    assertEquals(30, stack.size());
  }

  @Test
  public void testPeek() {
    for (int i = 0; i < 30; i++) {
      stack.push(i);
    }
    assertEquals(Integer.valueOf(29), stack.peek());
  }

  @Test
  public void testPop() {
    for (int i = 0; i < 30; i++) {
      stack.push(i);
    }
    for (int i = 29; i >= 0; i--) {
      assertEquals(Integer.valueOf(i), stack.pop());
    }
    assertTrue(stack.isEmpty());
  }

  @Test
  public void testIterator() {
    for (int i = 0; i < 30; i++) {
      stack.push(i);
    }
    Iterator<Integer> iterator = stack.iterator();
    StringBuilder itrBuilder = new StringBuilder();
    while (iterator.hasNext()) {
      itrBuilder.append(iterator.next());
      if (iterator.hasNext()) {
        itrBuilder.append(", ");
      }
    }
    StringBuilder popBuilder = new StringBuilder();
    for (int i = stack.size() - 1; i >= 0; i--) {
      popBuilder.append(stack.pop());
      if (i != 0) {
        popBuilder.append(", ");
      }
    }
    assertEquals(popBuilder.toString(), itrBuilder.toString());
  }
}
