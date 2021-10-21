package com.shawn.concurrent.eightlock;

import java.util.concurrent.TimeUnit;

/**
	2、sendEmail休眠3秒后 ，先执行 sendEmail 还是 sendSMS

   答案：sendEmail
   被 synchronized 修饰的方式，锁的对象是方法的调用者，所以说这里两个方法调用的对象是同一个
   先调用的先执行！
 @author shawn
 */
public class LockDemo02 {

	public static void main(String[] args) throws InterruptedException {
		Phone2 phone = new Phone2();

		new Thread(()->{
			try {
				phone.sendEmail();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"A").start();

		TimeUnit.SECONDS.sleep(2);

		new Thread(()->{
			phone.sendSMS();
		},"B").start();
	}

}

class Phone2{
	public synchronized void sendEmail() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		System.out.println("sendEmail");
	}

	public synchronized void sendSMS(){
		System.out.println("sendSMS");
	}
}
