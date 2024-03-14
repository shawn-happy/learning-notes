package io.github.shawn.datastructure.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import io.github.shawn.datastructure.queue.ArrayCircularQueue;
import io.github.shawn.datastructure.queue.Queue;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

/**
 * 循环队列测试类
 *
 * @author shawn
 * @since 2020/10/17
 */
public class ArrayCircularQueueTest {
  private Queue<Integer> queue;

  @Before
  public void init() {
    queue = new ArrayCircularQueue<>();
  }

  @Test
  public void test_enqueue() {
    for (int i = 0; i < 8; i++) {
      queue.offer(i + 1);
    }
    assertEquals(8, queue.size());
  }

  @Test
  public void test_size() {
    for (int i = 0; i < 10; i++) {
      queue.offer(i + 1);
    }
    assertEquals(10, queue.size());
    queue.poll();
    assertEquals(9, queue.size());
    queue.poll();
    assertEquals(8, queue.size());
    queue.offer(22);
    assertEquals(9, queue.size());
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
    assertEquals(2, queue.size());
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
