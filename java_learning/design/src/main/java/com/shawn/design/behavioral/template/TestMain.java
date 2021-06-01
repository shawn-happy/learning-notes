package com.shawn.design.behavioral.template;

public class TestMain {

  public static void main(String[] args) {
    //
    int[] arr = {2, 4, 5, 7, 6, 8};
    BubbleSort bubbleSort = new BubbleSort();
    System.out.println(bubbleSort.sortTime(arr));
  }
}
