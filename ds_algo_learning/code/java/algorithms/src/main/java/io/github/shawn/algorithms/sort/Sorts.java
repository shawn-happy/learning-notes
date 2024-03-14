package io.github.shawn.algorithms.sort;

public class Sorts {
  public static int[] bubbleSort(int[] array) {
    int n = array.length;
    for (int i = 0; i < n - 1; i++) {
      boolean flag = false;
      for (int j = 0; j < n - 1 - i; j++) {
        if (array[j] > array[j + 1]) {
          int tmp = array[j];
          array[j] = array[j + 1];
          array[j + 1] = tmp;
          flag = true;
        }
      }
      if (!flag) {
        break;
      }
    }
    return array;
  }
}
