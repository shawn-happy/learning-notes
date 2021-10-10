package com.shawn.study.big.data.spark.java.core.rdd.basic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class MapPartitionsWithIndexDemo {

  public static void main(String[] args) {
    SparkConf conf =
        new SparkConf().setAppName("map-partitions-with-index-demo").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 3);
    List<Integer> list =
        javaRDD
            .mapPartitionsWithIndex(
                (index, iterator) -> {
                  if (index == 1) {
                    return iterator;
                  } else {
                    return Collections.emptyIterator();
                  }
                },
                false)
            .collect();
    System.out.println(StringUtils.join(list, ", "));
    context.close();
  }
}
