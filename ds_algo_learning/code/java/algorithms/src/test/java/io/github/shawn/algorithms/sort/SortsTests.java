package io.github.shawn.algorithms.sort;

import org.junit.Assert;
import org.junit.Test;

public class SortsTests {

  private static final int[] array1 = {3, 5, 4, 1, 2, 6};
  private static final int[] expected1 = {1, 2, 3, 4, 5, 6};
  private static final int[] array2 = {4, 23, -6, 78, 1, 54, 23, -6, -231, 9, 12};
  private static final int[] expected2 = {
    -231, -6, -6, 1, 4, 9, 12, 23, 23, 54, 78,
  };

  @Test
  public void testBubbleSort() {
    Assert.assertArrayEquals(expected1, Sorts.bubbleSort(array1));
    Assert.assertArrayEquals(expected2, Sorts.bubbleSort(array2));
  }
}
