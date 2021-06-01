package com.shawn.concurrent.eightlock;

import java.util.concurrent.TimeUnit;

/**
	3、增加一个普通方法，请问先打印那个 sendEmail 还是 hello

   答案：hello
   新增加的这个方法没有 synchronized 修饰，不是同步方法，不受锁的影响！
 @author shawn
 */
public class LockDemo03 {

	public static void main(String[] args) throws InterruptedException {
		Phone3 phone = new Phone3();

		new Thread(()->{
			try {
				phone.sendEmail();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"A").start();

		//Thread.sleep(200);
		TimeUnit.SECONDS.sleep(1);

		new Thread(()->{
			phone.hello();
		},"B").start();
	}

}

class Phone3{
	public synchronized void sendEmail() throws InterruptedException {
		TimeUnit.SECONDS.sleep(4);
		System.out.println("sendEmail");
	}

	// 没有 synchronized 没有 static 就是普通方式
	public void hello(){
		System.out.println("hello");
	}
}
