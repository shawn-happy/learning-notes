package com.shawn.study.deep.in.java.design.behavioral.template;

import java.util.Random;

/**
 * 所有排序算法的父类
 *
 * @author shawn
 */
public abstract class AbstractSort implements Sort {

  protected void swap(int[] a, int i, int j) {
    int t = a[i];
    a[i] = a[j];
    a[j] = t;
  }

  protected Double sortTime(int[] a) {
    long c = System.currentTimeMillis();
    sort(a);
    return (System.currentTimeMillis() - c) / 1000.0;
  }

  protected Double avgSortTime(int turns, Long seed, int scale) {
    Double total = 0.0;

    Random seeds = seed == null ? new Random() : new Random(seed);

    for (int i = 0; i < turns; i++) {
      Random current = new Random(seeds.nextLong());
      int[] a = new int[scale];
      for (int j = 0; j < scale; j++) {
        a[j] = current.nextInt();
      }
      total += sortTime(a);
      assert isSorted(a);
    }

    return total / turns;
  }

  protected boolean less(int v, int w) {
    return v < w;
  }

  protected boolean isSorted(int[] a) {
    for (int i = 1; i < a.length; i++) {
      if (less(a[i], a[i - 1])) {
        return false;
      }
    }
    return true;
  }
}
