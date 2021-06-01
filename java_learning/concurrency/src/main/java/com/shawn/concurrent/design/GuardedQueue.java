package com.shawn.concurrent.design;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GuardedQueue {

	private static final Logger LOGGER = Logger.getLogger(GuardedQueue.class.getName());

	private final Queue<Integer> sourceList;

	public GuardedQueue() {
		this.sourceList = new LinkedList<>();
	}

	public synchronized Integer get() {
		while (sourceList.isEmpty()) {
			try {
				LOGGER.info("waiting");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("getting");
		return sourceList.peek();
	}

	/**
	 * @param e number which we want to put to our queue
	 */
	public synchronized void put(Integer e) {
		LOGGER.info("putting");
		sourceList.add(e);
		LOGGER.info("notifying");
		notify();
	}

	public static void main(String[] args) {
		GuardedQueue guardedQueue = new GuardedQueue();
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		executorService.execute(() -> {
				guardedQueue.get();
			}
		);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//now we execute second thread which will put number to guardedQueue and notify first thread that it could get
		executorService.execute(() -> {
				guardedQueue.put(20);
			}
		);
		executorService.shutdown();
		try {
			executorService.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
