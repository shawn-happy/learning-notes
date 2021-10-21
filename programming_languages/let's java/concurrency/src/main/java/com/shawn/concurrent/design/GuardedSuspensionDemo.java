package com.shawn.concurrent.design;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class GuardedSuspensionDemo {

}

class GuardedObject<T> {

	/**
	 * 受保护的对象
	 */
	T obj;
	final Lock lock = new ReentrantLock();
	final Condition done = lock.newCondition();
	final int timeout = 2;

	final static Map<Object, GuardedObject> map = new ConcurrentHashMap<>();

	static <K> GuardedObject create(K k){
		GuardedObject go = new GuardedObject();
		map.put(k,go);
		return go;
	}

	static <K, T> void fireEvent(K k, T obj){
		GuardedObject go = map.remove(k);
		if(go != null){
			go.onChanged(obj);
		}
	}

	/**
	 * 获取受保护对象
	 *
 	 * @param p
	 * @return
	 */
	T get(Predicate<T> p) {
		lock.lock();
		try {
			// MESA管程推荐写法
			while (!p.test(obj)) {
				done.await(timeout, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
		// 返回非空的受保护对象
		return obj;
	}

	/**
	 * 事件通知方法
	 *
	 * @param obj
	 */
	void onChanged(T obj) {
		lock.lock();
		try {
			this.obj = obj;
			done.signalAll();
		} finally {
			lock.unlock();
		}
	}
}

class Request{

	//该方法可以发送消息
	void send(Message msg){
		//省略相关代码
	}
	//MQ消息返回后会调用该方法
	//该方法的执行线程不同于
	//发送消息的线程
	void onMessage(Message msg){
		//唤醒等待的线程
		GuardedObject.fireEvent(msg.id, msg);
	}
	//处理浏览器发来的请求
	void handleWebReq(){
		String id = "";//序号生成器.get() + "";
		//创建一消息
		Message msg1 = new Message(id,"{...}");
		//创建GuardedObject实例
		GuardedObject<Message> go = GuardedObject.create(id);
		//发送消息
		send(msg1);
		//等待MQ消息
		Message r = go.get(t -> t != null);
	}

}


class Message{
	String id;
	String content;

	public Message(String id, String content) {
		this.id = id;
		this.content = content;
	}
}
