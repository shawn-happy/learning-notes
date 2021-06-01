package com.shawn.design.behavioral.template;

import java.util.Arrays;

/**
 * 插入排序（移动数据）
 *
 * @author shawn
 */
public class InsertionByDataMovingSort extends AbstractSort implements InsertionSort {

	@Override
	public void sort(int[] a) {
		int[] b = Arrays.copyOf(a, a.length);

		System.out.println("insertion sort by data moving, sort before elements: " + Arrays.toString(a));
		fromStartToEnd(a);
		System.out.println("insertion sort by data moving, sort after  elements: " + Arrays.toString(a));

		System.out.println("insertion sort by data moving, sort before elements: " + Arrays.toString(b));
		fromEndToStart(b);
		System.out.println("insertion sort by data moving, sort after  elements: " + Arrays.toString(b));
	}

	@Override
	public void fromEndToStart(int[] a) {
		int n = a.length;
		for (int i = 1; i < n; i++) {
			int insertion = a[i];
			int j = i - 1;
			while (j >= 0 && less(insertion, a[j])) {
				a[j + 1] = a[j];
				j--;
			}
			a[j + 1] = insertion;
		}
	}

	@Override
	public void fromStartToEnd(int[] a) {
		for (int i = 1; i < a.length; i++) {
			int value = a[i];

			int[] tmp = new int[2];
			int change = i;
			for (int j = 0; j < i; j++) {
				if (value >= a[j]) {
					continue;
				}

				int index = j % 2;
				if (change == i) {
					tmp[Math.abs(index - 1)] = a[j];
					change = j;
				}
				tmp[index] = a[j + 1];
				if (0 == index) {
					a[j + 1] = tmp[index + 1];
				} else {
					a[j + 1] = tmp[index - 1];
				}
			}
			a[change] = value;
		}
	}


}
