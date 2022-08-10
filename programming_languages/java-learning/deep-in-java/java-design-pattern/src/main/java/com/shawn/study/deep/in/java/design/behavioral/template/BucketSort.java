package com.shawn.study.deep.in.java.design.behavioral.template;

/**
 * 桶排序算法
 *
 * @author com.shawn
 */
public class BucketSort extends AbstractSort {

  @Override
  public void sort(int[] a) {
    bucketSort(a, a.length / 2);
  }

  /**
   * 桶排序
   *
   * @param arr 数组
   * @param bucketSize 桶容量
   */
  private void bucketSort(int[] arr, int bucketSize) {
    if (arr.length < 2) {
      return;
    }

    // 数组最小值
    int minValue = arr[0];
    // 数组最大值
    int maxValue = arr[1];
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] < minValue) {
        minValue = arr[i];
      } else if (arr[i] > maxValue) {
        maxValue = arr[i];
      }
    }

    // 桶数量
    int bucketCount = (maxValue - minValue) / bucketSize + 1;
    int[][] buckets = new int[bucketCount][bucketSize];
    int[] indexArr = new int[bucketCount];

    // 将数组中值分配到各个桶里
    for (int i = 0; i < arr.length; i++) {
      int bucketIndex = (arr[i] - minValue) / bucketSize;
      if (indexArr[bucketIndex] == buckets[bucketIndex].length) {
        ensureCapacity(buckets, bucketIndex);
      }
      buckets[bucketIndex][indexArr[bucketIndex]++] = arr[i];
    }

    // 对每个桶进行排序，这里使用了快速排序
    int k = 0;
    for (int i = 0; i < buckets.length; i++) {
      if (indexArr[i] == 0) {
        continue;
      }
      quickSortC(buckets[i], 0, indexArr[i] - 1);
      for (int j = 0; j < indexArr[i]; j++) {
        arr[k++] = buckets[i][j];
      }
    }
  }

  /** 数组扩容 */
  private void ensureCapacity(int[][] buckets, int bucketIndex) {
    int[] tempArr = buckets[bucketIndex];
    int[] newArr = new int[tempArr.length * 2];
    for (int j = 0; j < tempArr.length; j++) {
      newArr[j] = tempArr[j];
    }
    buckets[bucketIndex] = newArr;
  }

  /** 快速排序递归函数 */
  private void quickSortC(int[] arr, int p, int r) {
    if (p >= r) {
      return;
    }

    int q = partition(arr, p, r);
    quickSortC(arr, p, q - 1);
    quickSortC(arr, q + 1, r);
  }

  /**
   * 分区函数
   *
   * @return 分区点位置
   */
  private int partition(int[] arr, int p, int r) {
    int pivot = arr[r];
    int i = p;
    for (int j = p; j < r; j++) {
      if (arr[j] <= pivot) {
        swap(arr, i, j);
        i++;
      }
    }

    swap(arr, i, r);
    return i;
  }
}
