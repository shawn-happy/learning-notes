package com.shawn.design.behavioral.template;

import java.util.Arrays;

/**
 * 插入排序算法（数据交换）
 *
 * @author shawn
 */
public class InsertionBySwapSort extends AbstractSort implements InsertionSort {

	@Override
	public void sort(int[] a) {
		int[] b = Arrays.copyOf(a, a.length);

		System.out
			.println("insertion sort by swap element, sort before elements: " + Arrays.toString(a));
		fromStartToEnd(a);
		System.out
			.println("insertion sort by swap element, sort after  elements: " + Arrays.toString(a));

		System.out
			.println("insertion sort by swap element, sort before elements: " + Arrays.toString(b));
		fromEndToStart(b);
		System.out
			.println("insertion sort by swap element, sort after  elements: " + Arrays.toString(b));
	}

	@Override
	public void fromStartToEnd(int[] a) {
		int n = a.length;
		// 遍历未排序区间
		for (int i = 1; i < n; i++) {
			// 遍历已排序区间
			// 从左往右遍历
			for (int j = 0; j < i; j++) {
				if (less(a[i], a[j])) {
					swap(a, i, j);
				}
			}
		}
	}

	@Override
	public void fromEndToStart(int[] a) {
		int n = a.length;
		// 遍历未排序区间
		for (int i = 1; i < n; i++) {
			// 遍历已排序区间
			// 从右往左遍历
			for (int j = i; j > 0; j--) {
				if (less(a[i], a[j])) {
					swap(a, i, j);
				}
			}
		}
	}
}
