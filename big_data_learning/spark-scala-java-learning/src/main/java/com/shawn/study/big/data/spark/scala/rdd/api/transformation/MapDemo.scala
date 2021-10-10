package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.SparkContext

object MapDemo {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local[*]", "map-demo")
    val rdd = sc.makeRDD(Seq[Int](1, 2, 3, 4, 5, 6, 7, 8))
    rdd.map(num => num * 2).foreach(println)
    sc.stop()
  }

}
