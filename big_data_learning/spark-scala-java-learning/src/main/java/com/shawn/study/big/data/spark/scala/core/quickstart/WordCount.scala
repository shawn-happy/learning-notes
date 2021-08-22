package com.shawn.study.big.data.spark.scala.core.quickstart

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("spark-scala-learning").setMaster("local")
    val context = new SparkContext(conf)
    val path = "examples/input/wordcount"
    context.textFile(path).flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _).collect().foreach(println)
  }
}
