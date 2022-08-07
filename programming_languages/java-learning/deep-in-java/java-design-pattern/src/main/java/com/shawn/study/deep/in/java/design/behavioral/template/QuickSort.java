package com.shawn.study.deep.in.java.design.behavioral.template;

public class QuickSort extends AbstractSort {

	@Override
	public void sort(int[] a) {
		quickSortInternally(a, 0, a.length - 1);
	}

	/**
	 * 快速排序递归函数，p,r为下标
	 */
	private void quickSortInternally(int[] a, int p, int r) {
		if (p >= r) {
			return;
		}
		// 获取分区点
		int q = partition(a, p, r);
		quickSortInternally(a, p, q - 1);
		quickSortInternally(a, q + 1, r);
	}

	private int partition(int[] a, int p, int r) {
		int pivot = a[r];
		int i = p;

		for (int j = p; j < r; ++j) {
			if (less(a[j], pivot)) {
				if (i == j) {
					++i;
				} else {
					swap(a, i, j);
					i++;
				}
			}
		}
		swap(a, i, r);

		System.out.println("i = " + i);
		return i;
	}
}
