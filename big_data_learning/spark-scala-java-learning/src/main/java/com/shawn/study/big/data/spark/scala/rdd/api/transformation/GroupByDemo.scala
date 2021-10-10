package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GroupByDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("group-by-demo")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)
    val groupRDD: RDD[(Int, Iterable[Int])] = rdd.groupBy(num => num % 2)
    groupRDD.collect().foreach(println)
    sc.stop()
  }

}
