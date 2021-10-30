package com.shawn.study.leetcode.array;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwoSumTests {

  private static List<int[]> testCases = new ArrayList<>();
  private static List<Integer> targets = new ArrayList<>();

  static {
    testCases.add(new int[] {2, 7, 11, 15});
    testCases.add(new int[] {3, 2, 4});
    testCases.add(new int[] {3, 3});

    targets.add(9);
    targets.add(6);
    targets.add(6);
  }

  @Test
  public void testSolutionOne() {
    test("Approach 1: Brute Force: ", new TwoSumSolutionOne());
  }

  @Test
  public void testSolutionTwo() {
    test("Approach 2: Two Loop With Hash Table:", new TwoSumSolutionTwo());
  }

  @Test
  public void testSolutionThree() {
    test("Approach 3: One Loop With Hash Table:", new TwoSumSolutionThree());
  }

  @Test
  public void testSolutionFour() {
    test("Approach 4: Binary Search:", new TwoSumSolutionFour());
  }

  private static void test(String msg, TwoSum twoSum) {
    System.out.println(msg);
    for (int i = 0; i < testCases.size(); i++) {
      int[] testCase = testCases.get(i);
      int[] indices = twoSum.twoSum(testCase, targets.get(i));
      System.out.println(Arrays.toString(indices));
    }
  }
}
