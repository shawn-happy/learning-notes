package com.shawn.study.deep.in.java.concurrency.collection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue
 *
 * @author shawn
 */
public class SynchronousQueueDemo {

  public static void main(String[] args) throws Exception {
    BlockingQueue<Integer> queue = new SynchronousQueue<>();
    System.out.println("queue.offer(1): " + queue.offer(1));
    System.out.println("queue.offer(2): " + queue.offer(2));
    System.out.println("queue.offer(3): " + queue.offer(3));
    System.out.println("queue.take():   " + queue.take());
    System.out.println("queue.size():   " + queue.size());
  }
}
