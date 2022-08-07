package com.shawn.study.deep.in.java.design.create.singleton;

/**
 * 懒汉式 线程安全
 * @author com.shawn
 */
public class LazyMan {

	private static boolean flag = false;

	private static LazyMan lazyMan;

	private LazyMan() {
		synchronized (LazyMan.class) {
			if (flag == false) {
				flag = true;
			} else {
				throw new RuntimeException("不要试图用反射破坏单例模式");
			}
		}
	}

	public static synchronized LazyMan getInstance() {
		if (lazyMan == null) {
			lazyMan = new LazyMan();
		}
		return lazyMan;
	}

}
