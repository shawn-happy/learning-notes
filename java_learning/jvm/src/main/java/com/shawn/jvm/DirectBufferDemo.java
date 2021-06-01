package com.shawn.jvm;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import sun.misc.VM;

/**
 * -Xms10m -Xmx10m -XX:MaxDirectMemorySize=5m -XX:+PrintGCDetails
 * @author shawn
 */
public class DirectBufferDemo {

	public static void main(String[] args) throws Throwable {
		System.out.println("配置的MaxDirectMemorySize"
			+ VM.maxDirectMemory()/(double)1024/1024+"MB");

		TimeUnit.SECONDS.sleep(2);

		// ByteBuffer.allocate(); 分配JVM的堆内存，属于GC管辖
		// ByteBuffer.allocateDirect() ; // 分配本地OS内存，不属于GC管辖
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 1024 * 1024);
		// java.lang.OutOfMemoryError: Direct buffer memory
	}

}
