package com.shawn.study.deep.in.java.design.behavioral.template;

/**
 * 插入排序 接口（遍历顺序）
 *
 * @author com.shawn
 */
public interface InsertionSort {

	/**
	 * 插入排序，对已排序区间从左往右遍历
	 * @param a
	 */
	void fromStartToEnd(int[] a);

	/**
	 * 插入排序，对已排序区间从右往左遍历
	 * @param a
	 */
	void fromEndToStart(int[] a);

}
