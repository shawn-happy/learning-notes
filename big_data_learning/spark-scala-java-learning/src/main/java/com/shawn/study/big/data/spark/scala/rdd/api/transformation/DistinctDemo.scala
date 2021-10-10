package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object DistinctDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("distinct-demo")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4))
    val rdd1: RDD[Int] = rdd.distinct()
    rdd1.collect().foreach(println)
    sc.stop()
  }

}
