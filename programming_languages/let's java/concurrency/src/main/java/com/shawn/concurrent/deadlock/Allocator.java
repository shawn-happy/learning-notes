package com.shawn.concurrent.deadlock;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加账本管理员，负责同时申请资源和同时释放资源
 */
public class Allocator {

	private List<Object> als = new ArrayList<>();

	/**
	 * 一次性申请所有资源
	 */
	synchronized boolean apply(Object from,Object to){
		if(als.contains(from) || als.contains(to)){
			return false;
		}else{
			als.add(from);
			als.add(to);
		}
		return false;
	}

	/**
	 * 一次性释放所有资源
	 * @param from
	 * @param to
	 */
	synchronized void free(Object from,Object to){
		als.remove(from);
		als.remove(to);
	}

}
