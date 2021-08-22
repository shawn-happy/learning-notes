package com.shawn.study.big.data.spark.java.core.rdd;

import static com.shawn.study.big.data.spark.java.BaseAction.action;
import static com.shawn.study.big.data.spark.java.BaseAction.println;

public class ReadLogFiles {

  public static void main(String[] args) {
    getRequestPath();
  }

  private static void getRequestPath() {
    println(
        "get request path ",
        action(
            sparkContext ->
                sparkContext
                    .textFile("examples/input/apache.log")
                    .map(
                        line -> {
                          String[] data = line.split(" ");
                          return data[6];
                        })
                    .collect()));
  }
}
