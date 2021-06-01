package com.shawn.concurrent.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Thread POOL exception demo
 * @author shawn
 */
public class ThreadPoolExecutorExceptionDemo {

	public static void main(String[] args) throws Exception{
		ThreadPoolExecutor service = new ThreadPoolExecutor(
			1,1,0,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>()
		){
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				System.out.printf("当前线程[%s],遇到了异常，详细信息：[%s]\n",Thread.currentThread().getName(),t.getMessage());
			}
		};
		service.execute(()->{
			throw new RuntimeException("thread pool exception!!");
		});
		service.awaitTermination(1, TimeUnit.SECONDS);
		service.shutdown();


	}

}
