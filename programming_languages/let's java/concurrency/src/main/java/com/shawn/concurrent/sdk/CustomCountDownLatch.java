package com.shawn.concurrent.sdk;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomCountDownLatch {

	public static void main(String[] args) throws Exception{
//		countDownLatch();
//		countDownLatchBySynchronized();
		countDownLatchByLock();
	}

	private static void countDownLatch() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(5);
		ExecutorService service = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 4; i++) {
			service.submit(()->{
				action();
				latch.countDown();
			});
		}

		service.awaitTermination(50, TimeUnit.MILLISECONDS);
		latch.await();
		System.out.println("Done");
		service.shutdownNow();
	}

	private static void countDownLatchBySynchronized() throws InterruptedException {
		CountDownLatchBySynchronized latch = new CountDownLatchBySynchronized(5);
		ExecutorService service = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 5; i++) {
			service.submit(()->{
				action();
				latch.countDown();
			});
		}

		service.awaitTermination(50, TimeUnit.MILLISECONDS);
		latch.await();
		System.out.println("Done");
		service.shutdownNow();
	}

	private static void countDownLatchByLock() throws InterruptedException {
		CountDownLatchByLock latch = new CountDownLatchByLock(5);
		ExecutorService service = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 4; i++) {
			service.submit(()->{
				action();
				latch.countDown();
			});
		}

		service.awaitTermination(50, TimeUnit.MILLISECONDS);
		latch.await();
		System.out.println("Done");
		service.shutdownNow();
	}

	private static void action() {
		System.out.printf("当前线程[%s], 正在执行。。。\n", Thread.currentThread().getName());
	}

	private static class CountDownLatchBySynchronized{

		private int count;

		public CountDownLatchBySynchronized(int count){
			this.count = count;
		}

		public void countDown(){
			synchronized (this){
				if(count == 0){
					return;
				}
				count --;
				if(count == 0){
					notifyAll();
				}
			}
		}

		public void await() throws InterruptedException{
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			synchronized(this){
				while (count > 0){
					wait();
				}
			}

		}
	}

	private static class CountDownLatchByLock{

		private int count;

		private final Lock lock = new ReentrantLock();

		private final Condition condition = lock.newCondition();

		public CountDownLatchByLock(int count){
			this.count = count;
		}

		public void countDown(){
			lock.lock();
			try{
				if(count == 0){
					return;
				}
				count --;
				if(count == 0){
					condition.signalAll();
				}
			}finally {
				lock.unlock();
			}
		}

		public void await() throws InterruptedException{
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			lock.lock();
			try{
				while (count > 0){
					condition.await();
				}
			}finally {
				lock.unlock();
			}

		}
	}

}
