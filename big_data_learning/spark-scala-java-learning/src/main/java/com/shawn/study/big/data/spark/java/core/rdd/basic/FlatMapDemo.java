package com.shawn.study.big.data.spark.java.core.rdd.basic;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class FlatMapDemo {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("operator").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<List<Integer>> javaRDD =
        context.parallelize(
            Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5), Arrays.asList(6, 7, 8)), 2);
    List<Integer> list = javaRDD.flatMap(List::iterator).collect();
    System.out.println(StringUtils.join(list, ", "));
    context.close();
  }
}
