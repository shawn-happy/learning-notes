package com.shawn.concurrent.collection;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shawn
 * @param <T>
 */
public class BlockedQueue<T> {

	/**
	 * 最大容量
	 */
	private int capacity;

	/**
	 * 实际大小
	 */
	private int size;

	final Lock lock = new ReentrantLock();

	// 条件变量：队列不满
	final Condition notFull = lock.newCondition();

	// 条件变量: 队列不空
	final Condition notEmpty = lock.newCondition();

	/**
	 * 入队
	 * @param t
	 */
	void enqueue(T t){
		lock.lock();
		try {
			while (size == capacity){
				notFull.await();
			}
			// 入队操作省略
			// 通知出队操作
			notEmpty.signalAll();
		} catch (Exception e){

		} finally {
			lock.unlock();
		}
	}

	void dequeue(){
		lock.lock();
		try {
			while (size == 0){
				notEmpty.await();
			}
			// 出队操作省略
			// 通知入队操作
			notFull.signalAll();
		} catch (Exception e){

		} finally {
			lock.unlock();
		}
	}
}
