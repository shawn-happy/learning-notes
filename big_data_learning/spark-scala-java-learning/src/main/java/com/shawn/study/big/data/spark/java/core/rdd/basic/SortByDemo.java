package com.shawn.study.big.data.spark.java.core.rdd.basic;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SortByDemo {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("sort-demo").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 6, 5, 3, 4, 7, 2, 8), 2);
    final List<Integer> result = javaRDD.sortBy(num -> num, true, 2).collect();
    System.out.println(StringUtils.join(result, ", "));
    context.close();
  }
}
