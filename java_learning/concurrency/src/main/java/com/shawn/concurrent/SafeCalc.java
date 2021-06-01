package com.shawn.concurrent;

// 用两个不同的锁保护value,不满足互斥关系，不能保证可见性。
public class SafeCalc {

	static long value = 0L;

	synchronized long get() {
		return value;
	}

	synchronized static void addOne() {
		value += 1;
	}

}
