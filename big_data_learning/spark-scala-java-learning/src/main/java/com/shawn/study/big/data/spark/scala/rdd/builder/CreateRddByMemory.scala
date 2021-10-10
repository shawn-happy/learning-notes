package com.shawn.study.big.data.spark.scala.rdd.builder

import org.apache.spark.SparkContext

object CreateRddByMemory {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local[*]", "create-rdd-by-memory")
    val rdd = sc.makeRDD(Seq[Int](1, 2, 3))
    rdd.collect().foreach(println)
    sc.stop()
  }

}
