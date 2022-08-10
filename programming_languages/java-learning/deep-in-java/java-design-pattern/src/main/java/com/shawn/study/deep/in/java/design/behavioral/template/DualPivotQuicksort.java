package com.shawn.study.deep.in.java.design.behavioral.template;

/**
 * 双轴快速排序
 *
 * @author shawn
 */
public class DualPivotQuicksort extends AbstractSort {

  @Override
  public void sort(int[] a) {
    dualPivotQuicksort(a, 0, a.length - 1);
  }

  private void dualPivotQuicksort(int[] arr, int left, int right) {
    if (left >= right) {
      return;
    }
    int l = left;
    int k = left + 1;
    int r = right;
    // 判断pivot1 与 pivot2 大小
    if (arr[l] > arr[r]) {
      swap(arr, l, r);
    }
    int pivot1 = arr[l];
    int pivot2 = arr[r];

    while (k < r) {
      if (arr[k] < pivot1) {
        l++;
        if (l != k) {
          swap(arr, l, k);
        }
        k++;
      } else if (arr[k] >= pivot1 && arr[k] <= pivot2) {
        k++;
      } else {
        --r;
        if (arr[r] > pivot2) {

        } else if (arr[r] >= pivot1 && arr[r] <= pivot2) {
          swap(arr, k, r);
          k++;
        } else {
          l++;
          int tmp = arr[l];
          arr[l] = arr[r];
          arr[r] = arr[k];
          arr[k] = tmp;
          k++;
        }
      }
    }

    // 交换pivot1 和 pivot2
    arr[left] = arr[l];
    arr[l] = pivot1;
    arr[right] = arr[r];
    arr[r] = pivot2;

    dualPivotQuicksort(arr, left, l - 1);
    dualPivotQuicksort(arr, l + 1, r - 1);
    dualPivotQuicksort(arr, r + 1, right);
  }
}
