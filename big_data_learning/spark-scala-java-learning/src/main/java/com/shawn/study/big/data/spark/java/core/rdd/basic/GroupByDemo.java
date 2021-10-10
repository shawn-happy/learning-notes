package com.shawn.study.big.data.spark.java.core.rdd.basic;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class GroupByDemo {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("group-by-demo").setMaster("local[*]");
    JavaSparkContext context = new JavaSparkContext(conf);
    JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2);
    List<Tuple2<Integer, Iterable<Integer>>> list = javaRDD.groupBy(num -> num % 2).collect();
    System.out.println(StringUtils.join(list, ", "));
    context.close();
  }
}
