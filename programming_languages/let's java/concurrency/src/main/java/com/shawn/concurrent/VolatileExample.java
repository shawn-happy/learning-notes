package com.shawn.concurrent;

public class VolatileExample {

	int x = 0;
	volatile boolean v = false;
	public void write(){
		x = 42;
		v = true;
	}

	public void read(){
		if(v == true){
			System.out.println(x);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		VolatileExample example = new VolatileExample();
		Thread t1 = new Thread(()->{
			System.out.println(example.x);
			example.write();
		});

		Thread t2 = new Thread(()->{
			example.read();
			example.x = 11;
		});
		example.x = 45;
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(example.x);
	}

}
