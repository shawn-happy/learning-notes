package com.shawn.study.big.data.spark.java.core.rdd.basic;

import static com.shawn.study.big.data.spark.java.BaseAction.TEST_DATA_SET_1;
import static com.shawn.study.big.data.spark.java.BaseAction.TEST_DATA_SET_2;
import static com.shawn.study.big.data.spark.java.BaseAction.TEST_DATA_SET_3;
import static com.shawn.study.big.data.spark.java.BaseAction.TEST_DATA_SET_4;
import static com.shawn.study.big.data.spark.java.BaseAction.TEST_DATA_SET_5;
import static com.shawn.study.big.data.spark.java.BaseAction.action;
import static com.shawn.study.big.data.spark.java.BaseAction.print;

import java.util.Arrays;

public class TransformationAPITests {

  public static void main(String[] args) {
    map();
    map(2);
    flatMap();
    filter();
    distinct();
    union();
    intersection();
    subtract();
    cartesian();
    sample();
  }

  private static void map() {
    print(
        "transformation map API",
        action(
            sparkContext ->
                sparkContext.parallelize(TEST_DATA_SET_3).map(data -> data * 2).collect()));
  }

  private static void map(int numSlices) {
    print(
        "transformation map API",
        action(
            sparkContext ->
                sparkContext
                    .parallelize(TEST_DATA_SET_3, numSlices)
                    .map(data -> data * 2)
                    .collect()));
  }

  private static void flatMap() {
    print(
        "transformation flatMap API",
        action(
            sparkContext ->
                sparkContext
                    .parallelize(TEST_DATA_SET_4)
                    .flatMap(data -> Arrays.asList(data.split(" ")).iterator())
                    .collect()));
  }

  private static void filter() {
    print(
        "transformation filter API",
        action(
            sparkContext ->
                sparkContext.parallelize(TEST_DATA_SET_3).filter(data -> data % 2 != 0).collect()));
  }

  private static void distinct() {
    print(
        "transformation distinct API",
        action(sparkContext -> sparkContext.parallelize(TEST_DATA_SET_1).distinct().collect()));
  }

  private static void union() {
    print(
        "transformation union API",
        action(
            sparkContext ->
                sparkContext
                    .parallelize(TEST_DATA_SET_1)
                    .union(sparkContext.parallelize(TEST_DATA_SET_2))
                    .collect()));
  }

  private static void intersection() {
    print(
        "transformation intersection API",
        action(
            sparkContext ->
                sparkContext
                    .parallelize(TEST_DATA_SET_1)
                    .intersection(sparkContext.parallelize(TEST_DATA_SET_2))
                    .collect()));
  }

  private static void subtract() {
    print(
        "transformation subtract API",
        action(
            sparkContext ->
                sparkContext
                    .parallelize(TEST_DATA_SET_1)
                    .subtract(sparkContext.parallelize(TEST_DATA_SET_2))
                    .collect()));
  }

  private static void cartesian() {
    print(
        "transformation cartesian API",
        action(
            sparkContext ->
                sparkContext
                    .parallelize(TEST_DATA_SET_3)
                    .cartesian(sparkContext.parallelize(TEST_DATA_SET_5))
                    .collect()));
  }

  private static void sample() {
    print(
        "transformation sample API",
        action(
            sparkContext ->
                sparkContext.parallelize(TEST_DATA_SET_3).sample(false, 0.5).collect()));
  }
}
