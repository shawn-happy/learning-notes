package com.shawn.design.ioc;

/**
 * @author Shawn
 * @description
 * @since 2020/7/6
 */
public abstract class TestCase {

	public void run(){
		if(test()){
			System.out.println("Test succeed.");
		} else {
			System.out.println("Test failed.");
		}
	}

	public abstract boolean test();

}