package com.shawn.study.big.data.spark.java.core.rdd;

import static com.shawn.study.big.data.spark.java.BaseAction.action;
import static com.shawn.study.big.data.spark.java.BaseAction.actionWithoutResult;

import java.util.Arrays;
import java.util.List;

public class CreateRddTests {

  private static final String INPUT_LOCAL_PATH = "examples/input/wordcount";

  public static void main(String[] args) {
    createRddFromLocalFiles();
    createRddFromList();
  }

  private static void createRddFromLocalFiles() {
    actionWithoutResult(sparkContext -> sparkContext.textFile(INPUT_LOCAL_PATH));
    List<String> results =
        action(sparkContext -> sparkContext.textFile(INPUT_LOCAL_PATH).collect());
    results.forEach(System.out::println);
  }

  private static void createRddFromList() {
    List<Integer> results =
        action(sparkContext -> sparkContext.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6)).collect());
    results.forEach(System.out::println);
  }
}
