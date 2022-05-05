package com.shawn.study.big.data.spark.scala.core.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark_Pair_RDD_Transform_Operator {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("rdd_pair_operator")
    val sc = new SparkContext(conf)
    keys(sc)
    sc.stop()
  }

  def reduceByKey(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List((1, 2), (3, 4), (3, 6)))
    println(rdd.reduceByKey(_ + _).collect().mkString(","))
  }

  def reduceByKey2(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(("a", 2), ("a", 3), ("a", 4), ("b", 5)))
    println(rdd.reduceByKey((x: Int, y: Int) => {
      println(s"x = ${x}, y = ${y}")
      x + y
    }).collect().mkString(","))
  }

  def groupByKey(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List((1, 2), (3, 4), (3, 6)))
    println(rdd.groupByKey().collect().mkString(","))
  }

  def aggregateByKey(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3), ("a", 6)
    ), 2)
    println(rdd.aggregateByKey(5)((x, y) => math.max(x, y), (x, y) => x + y).collect().mkString(","))
    println(rdd.aggregateByKey(5)(_ + _, _ + _).collect().mkString(","))
    println(rdd.foldByKey(5)(_ + _).collect().mkString(","))
  }

  def combineByKey(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(("a", 1), ("a", 3), ("a", 6), ("b", 5), ("b", 7), ("b", 8)))
    println(rdd.combineByKey(
      v => (v, 1),
      (t: (Int, Int), v) => {
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    ).collect().mkString(","))
  }

  def mapValues(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(("a", 1), ("a", 3), ("a", 6), ("b", 5), ("b", 7), ("b", 8)), 2)
    println(rdd.mapValues(num => num + 1).collect().mkString(","))
  }

  def flatMapValues(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(("a", 1), ("a", 3), ("a", 6), ("b", 5), ("b", 7), ("b", 8)), 2)
    println(rdd.flatMapValues(num => num to 5).collect().mkString(","))
  }

  def keys(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(("a", 1), ("a", 3), ("a", 6), ("b", 5), ("b", 7), ("b", 8)), 2)
    println(rdd.keys.collect().mkString(","))
  }

  def values(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(("a", 1), ("a", 3), ("a", 6), ("b", 5), ("b", 7), ("b", 8)), 2)
    println(rdd.values.collect().mkString(","))
  }

  def sortByKey(sc: SparkContext): Unit = {
    val rdd = sc.makeRDD(List(("a", 1), ("a", 3), ("a", 6), ("b", 5), ("b", 7), ("b", 8)), 2)
    println(rdd.sortByKey().collect().mkString(","))
  }
}
