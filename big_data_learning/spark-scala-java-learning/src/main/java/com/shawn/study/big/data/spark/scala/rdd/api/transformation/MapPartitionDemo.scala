package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.{SparkContext, SparkConf}

object MapPartitionDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("map-partition-demo")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)
    val mpRDD = rdd.mapPartitions(
      iter => {
        List(iter.max).iterator
      }
    )
    mpRDD.collect().foreach(println)
    sc.stop()
  }

}
