package com.shawn.study.big.data.spark.java.core.rdd.builder;

import java.util.Arrays;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class CreateRddByMemory {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("create-rdd-by-memory").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<String> rdd = context.parallelize(Arrays.asList("1", "2", "3"));
    rdd.collect().forEach(System.out::println);
    context.close();
  }
}
