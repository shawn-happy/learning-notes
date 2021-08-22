package com.shawn.study.big.data.spark.java.core.quickstart;

import static com.shawn.study.big.data.spark.java.BaseAction.action;

import java.util.Arrays;
import java.util.List;
import scala.Tuple2;

public class SimpleJavaDemo {

  public static void main(String[] args) {
    String path = "examples/input/wordcount";
    List<Tuple2> results =
        action(
            sparkContext ->
                sparkContext
                    .textFile(path, sparkContext.defaultMinPartitions())
                    .flatMap(word -> Arrays.asList(word.split(" ")).iterator())
                    .mapToPair(word -> new Tuple2<>(word, 1))
                    .reduceByKey(Integer::sum)
                    .collect());
    results.forEach(System.out::println);
  }
}
