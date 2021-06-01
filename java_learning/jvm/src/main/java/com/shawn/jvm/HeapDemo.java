package com.shawn.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * VM -Args -Xms6m -Xmx6m -XX:+HeapDumpOnOutOfMemoryError
 * 演示运行时数据区-java heap中出现的OOM异常情况
 * @author shawn
 */
public class HeapDemo {

	public static void main(String[] args) {
		List<Object> list = new ArrayList<>();
		int i = 0;
		try {
			while (true){
				byte[] bytes = new byte[1024];
				list.add(bytes);
				i++;
			}
		} catch (Throwable e) {
			long maxMemory = Runtime.getRuntime().maxMemory();
			long totalMemory = Runtime.getRuntime().totalMemory();
			System.out.println("i = " + i);
			System.out.println("maxMemory = " + maxMemory);
			System.out.println("totalMemory = " + totalMemory);
			throw e;
		}
	}
}
