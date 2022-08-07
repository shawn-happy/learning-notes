package com.shawn.study.deep.in.java.design.behavioral.template;

import java.util.Arrays;

/**
 * 冒泡排序算法
 *
 * @author shawn
 */
public class BubbleSort extends AbstractSort {

	@Override
	public void sort(int[] a) {
		int[] b = Arrays.copyOf(a, a.length);
		int[] c = Arrays.copyOf(a, a.length);

		bubbleSort(a);
		bubbleSort2(b);
		bubbleDownSort(c);
	}

	private void bubbleSort(int[] a) {
		int n = a.length;
		for (int i = 0; i < n; i++) {
			// 当数组有序时，退出循环
			boolean flag = false;
			for (int j = 0; j < n - i - 1; j++) {
				if (less(a[j], a[j + 1])) {
					swap(a, j, j + 1);
					// 此次冒泡有数据交换
					flag = true;
				}
			}
			// 没有数据交换，提前退出
			if (!flag) {
				break;
			}
		}
	}

	private void bubbleSort2(int[] a) {
		int n = a.length;
		// 最后一次交换的位置
		int lastExchange = 0;
		// 无序数据的边界，每次只需要比较到这里即可退出
		int sortBorder = n - 1;
		for (int i = 0; i < n; i++) {
			// 提前退出标志位
			boolean flag = false;
			for (int j = 0; j < sortBorder; j++) {
				if (less(a[j], a[j + 1])) {
					swap(a, j, j + 1);
					// 此次冒泡有数据交换
					flag = true;
					// 更新最后一次交换的位置
					lastExchange = j;
				}
			}
			sortBorder = lastExchange;
			// 没有数据交换，提前退出
			if (!flag) {
				break;
			}
		}
	}

	private void bubbleDownSort(int[] a) {
		int len = a.length;
		for (int i = 0; i < len; i++) {
			boolean flag = false;
			for (int j = i + 1; j < len; j++) {
				if (less(a[i], a[j])) {
					swap(a, i, j);
					flag = true;
				}
			}
			if (!flag) {
				break;
			}
		}
	}
}
