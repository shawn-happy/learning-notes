package com.shawn.study.big.data.spark.scala.rdd.builder

import org.apache.spark.SparkContext

object CreateRddByLocalFile {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local[*]", "create-rdd-by-local-file")
    val rdd = sc.textFile("data/simple-data/1.txt")
    rdd.collect().foreach(println)
    sc.stop()
  }

}
