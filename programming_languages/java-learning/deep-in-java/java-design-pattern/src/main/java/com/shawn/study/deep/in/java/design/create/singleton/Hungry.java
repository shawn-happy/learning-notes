package com.shawn.study.deep.in.java.design.create.singleton;

/**
 * 基于饿汉式实现的单例模式
 * @author shao
 */
public class Hungry {

	private static final Hungry instance = new Hungry();

	/**
	 * 提供无参构造
	 */
	private Hungry(){

	}

	public Hungry getInstance(){
		return instance;
	}

}
