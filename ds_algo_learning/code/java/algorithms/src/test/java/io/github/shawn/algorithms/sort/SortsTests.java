package io.github.shawn.algorithms.sort;

import org.junit.Assert;
import org.junit.Test;

public class SortsTests {

  private static final int[] expected1 = {1, 2, 3, 4, 5, 6};
  private static final int[] expected2 = {
    -231, -6, -6, 1, 4, 9, 12, 23, 23, 54, 78,
  };

  @Test
  public void testBubbleSort() {
    final int[] array1 = {3, 5, 4, 1, 2, 6};
    Assert.assertArrayEquals(expected1, Sorts.bubbleSort(array1));
    final int[] array2 = {4, 23, -6, 78, 1, 54, 23, -6, -231, 9, 12};
    Assert.assertArrayEquals(expected2, Sorts.bubbleSort(array2));
  }

  @Test
  public void testInsertionSort() {
    final int[] array1 = {3, 5, 4, 1, 2, 6};
    Sorts.insertionSort(array1);
    Assert.assertArrayEquals(expected1, array1);
    final int[] array2 = {4, 23, -6, 78, 1, 54, 23, -6, -231, 9, 12};
    Sorts.insertionSort(array2);
    Assert.assertArrayEquals(expected2, array2);
  }

  @Test
  public void testSelectionSort() {
    final int[] array1 = {3, 5, 4, 1, 2, 6};
    Sorts.selectionSort(array1);
    Assert.assertArrayEquals(expected1, array1);
    final int[] array2 = {4, 23, -6, 78, 1, 54, 23, -6, -231, 9, 12};
    Sorts.selectionSort(array2);
    Assert.assertArrayEquals(expected2, array2);
  }

  @Test
  public void testShellSort() {
    final int[] array1 = {3, 5, 4, 1, 2, 6};
    Sorts.shellSort(array1);
    Assert.assertArrayEquals(expected1, array1);
    final int[] array2 = {4, 23, -6, 78, 1, 54, 23, -6, -231, 9, 12};
    Sorts.shellSort(array2);
    Assert.assertArrayEquals(expected2, array2);
  }

  @Test
  public void testMergeSort() {
    final int[] array1 = {3, 5, 4, 1, 2, 6};
    Sorts.mergeSort(array1);
    Assert.assertArrayEquals(expected1, array1);
    final int[] array2 = {4, 23, -6, 78, 1, 54, 23, -6, -231, 9, 12};
    Sorts.mergeSort(array2);
    Assert.assertArrayEquals(expected2, array2);
  }

  @Test
  public void testQuickSort() {
    final int[] array1 = {3, 5, 4, 1, 2, 6};
    Sorts.quickSort(array1);
    Assert.assertArrayEquals(expected1, array1);
    final int[] array2 = {4, 23, -6, 78, 1, 54, 23, -6, -231, 9, 12};
    Sorts.quickSort(array2);
    Assert.assertArrayEquals(expected2, array2);
  }
}
