package com.shawn.concurrent.sdk;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheData {

	Object data;

	volatile boolean cacheValid;

	final ReadWriteLock rwl = new ReentrantReadWriteLock();

	// 读锁
	final Lock r = rwl.readLock();

	//写锁
	final Lock w = rwl.writeLock();

	void processCacheData(){
		// 获取读锁
		r.lock();
		if(!cacheValid){
			// 释放读锁，因为不允许读锁的升级
			r.unlock();
			// 获取写锁
			w.lock();
			try {
				// 再次检查状态
				if(!cacheValid){
					// 处理数据
					cacheValid = true;
				}
				// 释放写锁前，降级为读锁
				r.lock();
			}finally {
				w.unlock();
			}
		}
		// 此处任有读锁
		try {
			// 使用数据
		}finally {
			r.unlock();
		}
	}

}
