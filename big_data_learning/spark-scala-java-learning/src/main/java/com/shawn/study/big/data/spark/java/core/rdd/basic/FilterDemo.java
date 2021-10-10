package com.shawn.study.big.data.spark.java.core.rdd.basic;

import java.util.Arrays;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class FilterDemo {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("filter-demo").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2);
    JavaRDD<Integer> filterRdd = javaRDD.filter(num -> num % 2 == 1);
    System.out.println(filterRdd.collect());
    context.close();
  }
}
