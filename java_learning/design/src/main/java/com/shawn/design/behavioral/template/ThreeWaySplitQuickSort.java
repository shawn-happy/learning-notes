package com.shawn.design.behavioral.template;

/**
 * 三向切分 快速排序
 *
 * @author shawn
 */
public class ThreeWaySplitQuickSort extends AbstractSort {

	@Override
	public void sort(int[] a) {
		threeWaySplitQuickSort(a, 0, a.length - 1);
	}

	private void threeWaySplitQuickSort(int[] arr, int left, int right) {
		if (left >= right) {
			return;
		}
		int l = left;
		int k = left + 1;
		int r = right;
		int pivot = arr[l];

		while (k <= r) {
			if (arr[k] < pivot) {
				swap(arr, l, k);
				l++;
				k++;
			} else if (arr[k] == pivot) {
				k++;
			} else {
				if (arr[r] > pivot) {
					r--;
				} else if (arr[r] == pivot) {
					swap(arr, k, r);
					k++;
					r--;
				} else {
					int tmp = arr[l];
					arr[l] = arr[r];
					arr[r] = arr[k];
					arr[k] = tmp;
					l++;
					k++;
					r--;
				}
			}
		}
		threeWaySplitQuickSort(arr, left, l - 1);
		threeWaySplitQuickSort(arr, r + 1, right);
	}
}
