package com.shawn.concurrent.thread;

import java.io.IOException;

/**
 * java创建进程demo
 * @author shawn
 */
public class ProcessCreatingDemo {

	public static void main(String[] args) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("calc");
	}

}
