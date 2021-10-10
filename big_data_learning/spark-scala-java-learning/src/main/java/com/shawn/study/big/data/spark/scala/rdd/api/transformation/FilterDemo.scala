package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object FilterDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1,2,3,4))
    val filterRDD: RDD[Int] = rdd.filter(num=>num%2!=0)
    filterRDD.collect().foreach(println)
    sc.stop()
  }

}
