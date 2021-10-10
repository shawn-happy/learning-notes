package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GlomDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("glom-demo")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)

    val glomRDD: RDD[Array[Int]] = rdd.glom()
    val maxRdd = glomRDD.map(array => {
      array.max
    })
    println(maxRdd.collect().sum)
    sc.stop()
  }

}
