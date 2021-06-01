package com.shawn.study.algorithms.datastructure.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shawn
 * @since 2020/10/16
 */
public class ArrayQueueTest {

  private Queue<Integer> queue;

  @Before
  public void init() {
    queue = new ArrayQueue<>();
  }

  @Test
  public void test_enqueue() {
    for (int i = 0; i < 8; i++) {
      queue.offer(i + 1);
    }
    assertEquals(8, queue.size());
  }

  @Test
  public void test_enqueue_full() {
    for (int i = 0; i < 11; i++) {
      queue.offer(i + 1);
    }
    assertEquals(11, queue.size());
  }

  @Test
  public void test_enqueue_2() {
    for (int i = 0; i < 10; i++) {
      queue.offer(i + 1);
    }
    assertEquals(Integer.valueOf(1), queue.poll());
    assertEquals(Integer.valueOf(2), queue.poll());
    queue.offer(11);
    assertEquals(9, queue.size());

  }

  @Test
  public void test_enqueue_move() {
    for (int i = 0; i < 10; i++) {
      queue.offer(i + 1);
    }
    for (int i = 0; i < 3; i++) {
      queue.poll();
    }
    for (int i = 0; i < 3; i++) {
      queue.offer((i + 1) * 10);
    }
    assertEquals(Integer.valueOf(4), queue.peek());
  }

  @Test
  public void test_peek() {
    for (int i = 0; i < 10; i++) {
      queue.offer(i + 1);
    }
    assertEquals(Integer.valueOf(1), queue.peek());
  }

  @Test
  public void test_dequeue() {
    for (int i = 0; i < 10; i++) {
      queue.offer(i + 1);
    }
    for (int i = 0; i < 8; i++) {
      assertEquals(Integer.valueOf(i + 1), queue.poll());
    }
  }

  @Test(expected = Exception.class)
  public void test_dequeue_empty() {
    for (int i = 0; i < 10; i++) {
      queue.offer(i + 1);
    }
    for (int i = 0; i < 11; i++) {
      if (i != 10) {
        assertEquals(Integer.valueOf(i + 1), queue.poll());
      } else {
        assertNull(queue.poll());
      }
    }
  }

  @Test
  public void test_iterator() {
    for (int i = 0; i < 8; i++) {
      queue.offer(i + 1);
    }
    Iterator<Integer> iterator = queue.iterator();
    while (iterator.hasNext()) {
      Integer next = iterator.next();
      System.out.print(next);
      if (iterator.hasNext()) {
        System.out.print(", ");
      }
    }
    System.out.println();
    assertEquals(Integer.valueOf(1), queue.poll());
    Iterator<Integer> iterator2 = queue.iterator();
    while (iterator2.hasNext()) {
      Integer next = iterator2.next();
      System.out.print(next);
      if (iterator2.hasNext()) {
        System.out.print(", ");
      }
    }
    System.out.println();
  }
}
