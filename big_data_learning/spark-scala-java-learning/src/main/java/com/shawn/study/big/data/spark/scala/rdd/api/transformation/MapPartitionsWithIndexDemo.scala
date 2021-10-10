package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.{SparkConf, SparkContext}

object MapPartitionsWithIndexDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("map-partitions-with-index-demo")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4), 2)
    // 【1，2】，【3，4】
    val mpiRDD = rdd.mapPartitionsWithIndex(
      (index, iter) => {
        if ( index == 0 ) {
          iter
        } else {
          Nil.iterator
        }
      }
    )
    mpiRDD.collect().foreach(println)
    sc.stop()
  }

}
