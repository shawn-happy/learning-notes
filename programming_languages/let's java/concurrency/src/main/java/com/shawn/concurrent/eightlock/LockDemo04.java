package com.shawn.concurrent.eightlock;

import java.util.concurrent.TimeUnit;

/**
	4、两个手机，请问先执行sendEmail 还是 sendSMS
    答案：sendSMS
    被 synchronized  修饰的方式，锁的对象是调用者；我们这里有两个调用者，两个方法在这里是两个锁
 @author shawn
 */
public class LockDemo04 {
	public static void main(String[] args) throws InterruptedException {
		Phone4 phone1 = new Phone4();
		Phone4 phone2 = new Phone4();

		new Thread(()->{
			try {
				phone1.sendEmail();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"A").start();

		TimeUnit.SECONDS.sleep(1);

		new Thread(()->{
			phone2.sendSMS();
		},"B").start();
	}
}

class Phone4{
	public synchronized void sendEmail() throws InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		System.out.println("sendEmail");
	}

	public synchronized void sendSMS(){
		System.out.println("sendSMS");
	}
}