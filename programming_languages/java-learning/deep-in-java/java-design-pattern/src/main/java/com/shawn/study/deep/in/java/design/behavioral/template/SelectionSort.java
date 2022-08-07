package com.shawn.study.deep.in.java.design.behavioral.template;

/**
 * 选择排序
 *
 * @author com.shawn
 */
public class SelectionSort extends AbstractSort {

	@Override
	public void sort(int[] a) {
		int n = a.length;
		for (int i = 0; i < n - 1; ++i) {
			// 查找最小值
			int minIndex = i;
			for (int j = i + 1; j < n; ++j) {
				if (less(a[j], a[minIndex])) {
					minIndex = j;
				}
			}
			// 交换
			swap(a, i, minIndex);
		}
	}
}
