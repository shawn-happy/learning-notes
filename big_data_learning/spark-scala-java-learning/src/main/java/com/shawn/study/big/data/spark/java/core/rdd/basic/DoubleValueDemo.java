package com.shawn.study.big.data.spark.java.core.rdd.basic;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class DoubleValueDemo {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("sort-demo").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<Integer> rdd1 = context.parallelize(Arrays.asList(1, 2, 3, 4));
    JavaRDD<Integer> rdd2 = context.parallelize(Arrays.asList(3, 4, 5, 6));
    System.out.println(StringUtils.join(rdd1.intersection(rdd2).collect(), ", "));
    System.out.println(StringUtils.join(rdd1.union(rdd2).collect(), ", "));
    System.out.println(StringUtils.join(rdd1.subtract(rdd2).collect(), ", "));
    System.out.println(StringUtils.join(rdd1.zip(rdd2).collect(), ", "));
    context.close();
  }
}
