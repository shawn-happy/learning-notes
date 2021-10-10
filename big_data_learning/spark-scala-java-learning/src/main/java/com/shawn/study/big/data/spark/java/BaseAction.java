package com.shawn.study.big.data.spark.java;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class BaseAction {

  public static final List<String> TEST_DATA_SET_1 =
      Arrays.asList("Coffee", "Coffee", "Panda", "Monkey", "Tea");
  public static final List<String> TEST_DATA_SET_2 = Arrays.asList("Coffee", "Monkey", "Kitty");
  public static final List<Integer> TEST_DATA_SET_3 = Arrays.asList(5, 3, 1, 2, 6, 4);
  public static final List<String> TEST_DATA_SET_4 =
      Arrays.asList("Hello World", "Hello Spark", "Hello Java", "Java Learning", "Spark Learning");
  public static final List<Integer> TEST_DATA_SET_5 = Arrays.asList(6, 8, 5, 3);

  public static void actionWithoutResult(SparkContextActionWithoutResult sparkContextAction) {
    final SparkConf conf = new SparkConf().setAppName("spark-java-learning").setMaster("local[*]");
    JavaSparkContext sparkContext = null;
    try {
      sparkContext = new JavaSparkContext(conf);
      sparkContextAction.actionWithoutResult(sparkContext);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (sparkContext != null) {
        sparkContext.close();
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static <R> R action(SparkContextAction sparkContextAction) {
    final SparkConf conf = new SparkConf().setAppName("spark-java-learning").setMaster("local");
    JavaSparkContext sparkContext = null;
    try {
      sparkContext = new JavaSparkContext(conf);
      return (R) sparkContextAction.action(sparkContext);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (sparkContext != null) {
        sparkContext.close();
      }
    }
  }

  public static <T> void print(String msg, List<T> results) {
    System.out.println(msg + ": " + StringUtils.join(results, ","));
  }

  public static <T> void println(String msg, List<T> results) {
    System.out.println(msg + ": ");
    results.forEach(System.out::println);
  }
}
