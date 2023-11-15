package com.shawn.study.deep.in.java.jvm;

import java.util.Arrays;
import java.util.Random;

public class ControlTests {

  public void forLoop() {
    int a = 49;
    for (int i = 0; i < 100; i++) {
      if (i == a) {
        continue;
      } else if (i == 56) {
        break;
      }
    }
  }

  public void forEachLoop() {
    int[] arr = new int[10];
    Arrays.fill(arr, 1);
    for (int i : arr) {
      System.out.println(i);
    }
  }

  public void whileLoop() {
    int[] arr = new int[10];
    Arrays.fill(arr, 1);
    int i = 0;
    while (i < arr.length) {
      System.out.println(arr[i]);
      i++;
    }
  }

  public void doWhileLoop() {
    int[] arr = new int[10];
    Arrays.fill(arr, 1);
    int i = 0;
    do {
      System.out.println(arr[i]);
      i++;
    } while (i < arr.length);
  }

  public void switchCase() {
    int i = new Random().nextInt(2);
    switch (i) {
      case 0:
        System.out.println(0);
        break;
      case 1:
        System.out.println(1);
        break;
      default:
        System.out.println("default");
        break;
    }
  }
}
