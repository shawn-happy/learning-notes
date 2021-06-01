package com.shawn.concurrent.design;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPerMessageDemo {

}

class Server {

	public static void main(String[] args) throws IOException {
		final ServerSocketChannel ssc = ServerSocketChannel.open()
			.bind(new InetSocketAddress(8080));

		// 处理请求
		try {
			while (true) {
				SocketChannel accept = ssc.accept();
				// 每一个请求都创建一个线程

				new Thread(() -> {
					try {
						// 读socket
						ByteBuffer rb = ByteBuffer.allocateDirect(1024);
						accept.read(rb);
						// 模拟处理请求
						TimeUnit.SECONDS.sleep(2);
						// 写socket
						ByteBuffer wb = (ByteBuffer) rb.flip();
						accept.write(wb);
						accept.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}).start();
			}
		} finally {
			ssc.close();
		}
	}

}