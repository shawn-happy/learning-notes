package com.shawn.study.deep.in.java.design.create.singleton;

import java.lang.reflect.Constructor;

/**
 * 测试单例
 * @author com.shawn
 */
public class TestMain {

	public static void main(String[] args) {
		try {
			LazyMan lazyMan1 = LazyMan.getInstance();
			Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
			declaredConstructor.setAccessible(true);
			LazyMan lazyMan2 = declaredConstructor.newInstance();
			System.out.println(lazyMan1.hashCode());
			System.out.println(lazyMan2.hashCode());
			System.out.println(lazyMan1 == lazyMan2);
		} catch (Exception e) {
			e.printStackTrace();
		}
			for (int i = 0; i < 10000; i++) {
				Thread t = new Thread(
					()->{
						System.out.println(DoubleCheckedLocking.getSingleton().hashCode());
					}
				);
				t.start();
			}
	}

}
