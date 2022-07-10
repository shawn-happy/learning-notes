package com.shawn.design.create.singleton;

/**
 * Static inner class
 * @author com.shawn
 */
public class StaticInnerClass {

	private StaticInnerClass(){

	}

	public static StaticInnerClass getInstance(){
		return InnerClass.INTANCE;
	}

	private static class InnerClass{

		private static final StaticInnerClass INTANCE = new StaticInnerClass();

	}

}
