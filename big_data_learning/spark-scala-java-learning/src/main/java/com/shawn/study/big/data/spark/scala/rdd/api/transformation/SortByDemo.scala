package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object SortByDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sort-demo")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1, 6, 5, 3, 4, 7, 2, 8))
    val sortRdd: RDD[Int] = rdd.sortBy(num => num)
    sortRdd.collect().foreach(println)
    sc.stop()
  }

}
