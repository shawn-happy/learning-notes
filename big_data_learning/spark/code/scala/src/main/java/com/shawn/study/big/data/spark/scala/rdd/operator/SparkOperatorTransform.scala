package com.shawn.study.big.data.spark.scala.rdd.operator

import org.apache.spark.{SparkConf, SparkContext}

object SparkOperatorTransform {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("rdd_operator")
    val sc = new SparkContext(conf)
    map(sc)
    mapWithPartition(sc)
    sc.stop()
  }

  /**
   * pair operator: map
   *
   * @param sc
   */
  def map(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    rdd.map(_ * 2).collect().foreach(println)
  }

  /**
   * pair operator: map
   *
   * @param sc
   */
  def mapWithPartition(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)
    rdd.saveAsTextFile("../data/output/scala/spark_operator_map_with_partition1")
    rdd.map(_ * 2).saveAsTextFile("../data/output/scala/spark_operator_map_with_partition2")
  }

}
