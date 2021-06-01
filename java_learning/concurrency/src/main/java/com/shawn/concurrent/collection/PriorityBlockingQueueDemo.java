package com.shawn.concurrent.collection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author shawn
 */
public class PriorityBlockingQueueDemo{

    public static void main(String[] args) throws Exception{
        BlockingQueue queue = new PriorityBlockingQueue<>(2);
        queue.put(3);
        queue.put(2);
        queue.put(1);
        System.out.println("size: " + queue.size());
        System.out.println("take: " + queue.take());
        System.out.println("queue: " + queue);
    }

}