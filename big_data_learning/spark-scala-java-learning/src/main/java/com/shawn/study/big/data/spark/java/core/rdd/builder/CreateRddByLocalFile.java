package com.shawn.study.big.data.spark.java.core.rdd.builder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class CreateRddByLocalFile {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("create-rdd-by-local-file").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<String> rdd = context.textFile("data/simple-data/1.txt");
    rdd.collect().forEach(System.out::println);
    context.close();
  }
}
