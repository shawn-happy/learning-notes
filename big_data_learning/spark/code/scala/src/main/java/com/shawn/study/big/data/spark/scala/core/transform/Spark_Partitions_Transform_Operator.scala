package com.shawn.study.big.data.spark.scala.core.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Partitions_Transform_Operator {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("rdd_partition_operator")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7), 3)
    val log = sc.textFile("../../../data/apache.txt")
    repartition(rdd)
    sc.stop()
  }

  def coalesce(rdd: RDD[Int]): Unit = {
    rdd.coalesce(2, shuffle = true).saveAsTextFile("../../output/spark_partitions_transform_operator_coalesce")
  }

  def repartition(rdd: RDD[Int]): Unit = {
    rdd.repartition(4).saveAsTextFile("../../output/spark_partitions_transform_operator_repartition")
  }
}
