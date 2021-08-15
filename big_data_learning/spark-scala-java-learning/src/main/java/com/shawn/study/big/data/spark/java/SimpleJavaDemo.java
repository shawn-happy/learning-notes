package com.shawn.study.big.data.spark.java;

import java.util.Arrays;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

public class SimpleJavaDemo {

  public static void main(String[] args) {
    final SparkConf sparkConf = new SparkConf();
    sparkConf.setAppName("spark-java-learning")
        .setMaster("local");
    final SparkContext sparkContext = new SparkContext(sparkConf);
    final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
    
  }

}
