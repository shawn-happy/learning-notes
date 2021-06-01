package com.shawn.concurrent.thread;

public class ThreadGroupDemo {

	public static void main(String[] args) {
		Thread t1 = new Thread(ThreadGroupDemo::action,"t1");
		Thread t2 = new Thread(ThreadGroupDemo::action,"t2");
		Thread t3 = new Thread(ThreadGroupDemo::action,"t3");
		t1.start();
		t2.start();
		t3.start();

		Thread main = Thread.currentThread();
		ThreadGroup threadGroup = main.getThreadGroup();
		// 获取活跃线程数
		int count = threadGroup.activeCount();
		Thread[] threads = new Thread[count];
		threadGroup.enumerate(threads,true);

		for (Thread thread: threads) {
			System.out.printf("当前活跃的线程: [%s]\n",thread.getName());
		}

	}

	private static void action(){
		System.out.printf("当前线程[%s], 正在执行。。。\n",Thread.currentThread().getName());
	}

}
