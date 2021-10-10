package com.shawn.study.big.data.spark.java.core.rdd.basic;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class KeyValueDemo {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("key-value-demo").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    //    context.f
    context.close();
  }
}
