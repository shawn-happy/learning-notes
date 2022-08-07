package com.shawn.study.deep.in.java.design.behavioral.template;

/**
 * 堆排序算法
 *
 * @author shawn
 */
public class HeapSort extends AbstractSort {

	private int[] x;

	@Override
	public void sort(int[] a) {
		this.x = a;
		int n = x.length;

		// build the heap
		for (int i = (n - 2) / 2; i >= 0; i--) {
			sink(i, n);
		}

		// remove x[0] to x[last index]
		while (n > 1) {
			swap(x, 0, --n);
			sink(0, n);
		}
	}

	private void sink(int k, int boundary) {
		while (2 * k + 1 < boundary) {
			// only one leaf node
			if (2 * k + 1 == boundary - 1) {
				if (less(x[k], x[2 * k + 1])) {
					swap(x, k, 2 * k + 1);
				}
				break;
			} else {
				if (less(x[2 * k + 1], x[2 * k + 2])) {
					if (less(x[2 * k + 2], x[k])) {
						break;
					}
					swap(x, k, 2 * k + 2);
					k = 2 * k + 2;
				} else {
					if (less(x[2 * k + 1], x[k])) {
						break;
					}
					swap(x, k, 2 * k + 1);
					k = 2 * k + 1;
				}
			}
		}
	}


}
