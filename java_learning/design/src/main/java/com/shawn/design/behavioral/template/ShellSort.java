package com.shawn.design.behavioral.template;

/**
 * 希尔排序
 *
 * @author com.shawn
 */
public class ShellSort extends AbstractSort {

	@Override
	public void sort(int[] a) {
		int len = a.length;
		if (len == 1) {
			return;
		}
		int step = len / 2;
		while (step >= 1) {
			for (int i = step; i < len; i++) {
				int value = a[i];
				int j = i - step;
				for (; j >= 0; j -= step) {
					if (value < a[j]) {
						a[j + step] = a[j];
					} else {
						break;
					}
				}
				a[j + step] = value;
			}
			step = step / 2;
		}
	}
}
