package com.shawn.study.big.data.spark.scala.core.numeric

import org.apache.spark.SparkContext

object Spark_Numeric_Operator {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "Spark Numeric Operator")
    val rdd = sc.makeRDD(List(1, 3, 5, 6, 2, 7, 4, 8))
    val max = rdd.max()
    val min = rdd.min()
    val count = rdd.count()
    val sum = rdd.sum()
    val mean = rdd.mean()
    val variance = rdd.variance()
    val sampleVariance = rdd.sampleVariance()
    val stdev = rdd.stdev()
    val sampleStdev = rdd.sampleStdev()

    printf("max: %d\n" +
      "min: %d\n" +
      "count: %d\n" +
      "sum: %f\n" +
      "mean: %f\n" +
      "variance: %f\n" +
      "sample Variance: %f\n" +
      "Standard deviation: %f\n" +
      "Sample Standard deviation: %f \n",
      max, min, count, sum, mean, variance, sampleVariance, stdev, sampleStdev)

    sc.stop()
  }
}
