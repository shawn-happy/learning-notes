package com.shawn.study.leetcode.array;

import java.util.*;

/**
 * <a href="https://leetcode.com/problems/two-sum/">two sum</a>
 *
 * @author Shawn
 */
public interface TwoSum {
  int[] twoSum(int[] nums, int target);
}

/**
 * Brute Force
 *
 * @author Shawn
 */
class TwoSumSolutionOne implements TwoSum {

  @Override
  public int[] twoSum(int[] nums, int target) {
    for (int i = 0; i < nums.length; i++) {
      int num = nums[i];
      for (int j = i + 1; j < nums.length; j++) {
        if (nums[j] == target - num) {
          return new int[] {i, j};
        }
      }
    }
    return null;
  }
}

/** Two Loop With Hash Table */
class TwoSumSolutionTwo implements TwoSum {
  @Override
  public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      map.put(nums[i], i);
    }
    for (int i = 0; i < nums.length; i++) {
      int num = target - nums[i];
      if (map.containsKey(num) && map.get(num) != i) {
        return new int[] {i, map.get(num)};
      }
    }
    return null;
  }
}

/** One Loop With Hash Table */
class TwoSumSolutionThree implements TwoSum {

  @Override
  public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      int num = target - nums[i];
      if (map.containsKey(num)) {
        return new int[] {map.get(num), i};
      }
      map.put(nums[i], i);
    }
    return null;
  }
}

/** binary search */
class TwoSumSolutionFour implements TwoSum {

  @Override
  public int[] twoSum(int[] nums, int target) {
    int[] copyNums = Arrays.copyOf(nums, nums.length);
    Arrays.sort(copyNums);
    int left = 0, right = copyNums.length - 1;
    int[] indices = new int[2];
    while (left < right) {
      int sum = copyNums[left] + copyNums[right];
      if (sum == target) {
        indices[0] = left;
        indices[1] = right;
        break;
      } else if (sum < target) {
        left++;
      } else {
        right--;
      }
    }
    for (int i = 0; i < nums.length; i++) {
      if (copyNums[indices[0]] == nums[i]) {
        indices[0] = i;
        break;
      }
    }

    for (int i = nums.length - 1; i >= 0; i--) {
      if (copyNums[indices[1]] == nums[i]) {
        indices[1] = i;
        break;
      }
    }

    return indices;
  }
}
