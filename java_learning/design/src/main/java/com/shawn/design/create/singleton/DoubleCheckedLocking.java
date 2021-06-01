package com.shawn.design.create.singleton;

/**
 * double-checked locking
 * @author  com.shawn
 */
public class DoubleCheckedLocking {

	private static DoubleCheckedLocking singleton;

	private DoubleCheckedLocking (){}

	public static DoubleCheckedLocking getSingleton() {
		if (singleton == null) {
			synchronized (DoubleCheckedLocking.class) {
				if (singleton == null) {
					singleton = new DoubleCheckedLocking();
				}
			}
		}
		return singleton;
	}

}
