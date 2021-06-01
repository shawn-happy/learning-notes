package com.shawn.concurrent;

public class Singleton {

	private static Singleton singleton;

	private Singleton(){

	}

	public static Singleton getInstance(){
		if(singleton == null){
			synchronized(Singleton.class){
				if(singleton == null){
					// 创建对象的指令重排
					// 分配一个内存M                     分配一个内存M
					// 在内存m上初始化singleton对象       然后M的地址值赋值给singleton变量
					// 然后M的地址值赋值给singleton变量   在内存m上初始化singleton对象
					singleton = new Singleton();
					return singleton;
				}
			}
		}
		return singleton;
	}

}
