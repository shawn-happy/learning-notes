package io.github.shawn.datastructure.queue;

/**
 * 双端队列的抽象数据类型
 *
 * @param <T>
 */
public interface Deque<T> extends Queue<T> {
  void offerFirst(T t);

  void offerLast(T t);

  T pollFirst();

  T pollLast();

  T peekFirst();

  T peekLast();
}
