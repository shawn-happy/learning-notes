package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.{SparkConf, SparkContext}

object SampleDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    rdd.sample(true, 0.5).collect().foreach(println)
    sc.stop()
  }

}

