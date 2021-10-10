package com.shawn.study.big.data.spark.java.core.rdd.basic;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class DistinctDemo {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("sample-demo").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 1, 2, 3, 3, 4, 4), 2);
    final List<Integer> result = javaRDD.distinct().collect();
    System.out.println(StringUtils.join(result, ", "));
    context.close();
  }
}
