package io.github.shawn.algorithms.sort;

public class Sorts {

  public static int[] bubbleSort(int[] array) {
    if (array == null || array.length <= 1) {
      return array;
    }
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

  public static void insertionSort(int[] array) {
    if (array == null || array.length <= 1) {
      return;
    }
    for (int i = 1; i < array.length; i++) {
      int value = array[i];
      int j = i - 1;
      for (; j >= 0; j--) {
        if (array[j] > value) {
          array[j + 1] = array[j];
        } else {
          break;
        }
      }
      array[j + 1] = value;
    }
  }

  public static void selectionSort(int[] array) {
    if (array == null || array.length <= 1) {
      return;
    }
    int min;
    for (int i = 0; i < array.length - 1; i++) {
      min = i;

      for (int j = array.length - 1; j > i; j--) {
        if (array[j] < array[min]) {
          min = j;
        }
      }
      if (min != i) {
        int tmp = array[i];
        array[i] = array[min];
        array[min] = tmp;
      }
    }
  }

  public static void shellSort(int[] arr) {
    if (arr == null || arr.length <= 1) {
      return;
    }
    int step = arr.length / 2;
    while (step >= 1) {
      for (int i = step; i < arr.length; i++) {
        int value = arr[i];
        int j = i - step;
        for (; j >= 0; j -= step) {
          if (value < arr[j]) {
            arr[j + step] = arr[j];
          } else {
            break;
          }
        }
        arr[j + step] = value;
      }
      step /= 2;
    }
  }
}
