package com.shawn.study.big.data.spark.scala.core.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat

object Spark_Basic_Transform_Operator {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("rdd_operator")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)
    val log = sc.textFile("../../data/apache.txt")
    //    groupBy_apache_log(sc, log)
    //    filter_apache_log(sc, log)
    //    distinct(sc, log)
    sortBy_test(sc, log)
    sc.stop()
  }

  /**
   * pair operator: map
   *
   * @param sc
   */
  def map(sc: SparkContext, rdd: RDD[Int]): Unit = {
    rdd.map(_ * 2).collect().foreach(println)
  }

  /**
   * pair operator: map
   *
   * @param sc
   */
  def mapWithPartition(sc: SparkContext, rdd: RDD[Int]): Unit = {
    rdd.saveAsTextFile("../../output/scala/spark_operator_map_with_partition1")
    rdd.map(_ * 2).saveAsTextFile("../../output/scala/spark_operator_map_with_partition2")
  }

  def mapPartition(sc: SparkContext, rdd: RDD[Int]): Unit = {
    rdd.mapPartitions(iter => {
      List(iter.max).iterator
    }).collect().foreach(println)
  }

  def mapPartitionsWithIndex(sc: SparkContext, rdd: RDD[Int]): Unit = {
    rdd.mapPartitionsWithIndex((index, iter) => {
      if (index == 1) {
        iter
      } else {
        List(iter.max).iterator
      }
    }).collect().foreach(println)
  }

  def glom(sc: SparkContext, rdd: RDD[Int]): Unit = {
    val glomRDD = rdd.glom()
    val maxRdd = glomRDD.map(array => {
      array.max
    })
    println(maxRdd.collect().sum)
  }

  def groupBy(sc: SparkContext, rdd: RDD[Int]): Unit = {
    rdd.groupBy(num => num % 3).collect().foreach(println)

    val source = sc.makeRDD(List("Hello", "Spark", "Scala", "hadoop", "hive"), 2)
    source.groupBy(_.charAt(0)).collect().foreach(println)
  }

  def groupBy_apache_log(sc: SparkContext, log: RDD[String]): Unit = {
    log.map(line => {
      val data = line.split(" ")
      val time = data(3)
      val sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
      val date = sdf.parse(time)
      val sdf1 = new SimpleDateFormat("dd/MM/yyyy")
      val hour = sdf1.format(date)
      (hour, 1)
    }).groupBy(_._1).map({
      case (hour, iter) =>
        (hour, iter.size)
    }).collect().foreach(println)
  }

  def filter(sc: SparkContext, rdd: RDD[Int]): Unit = {
    rdd.filter(_ % 2 == 0).collect().foreach(println)
  }

  def filter_apache_log(sc: SparkContext, log: RDD[String]): Unit = {
    val count = log.filter(line => {
      val data = line.split(" ")
      val time = data(3)
      time.startsWith("17/05/2015")
    }).count()
    println(count)
  }

  def sample(sc: SparkContext, log: RDD[String]): Unit = {
    val rdd = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    println(rdd.sample(withReplacement = true, 0.6).collect().mkString(","))
  }

  def distinct(sc: SparkContext, log: RDD[String]): Unit = {
    val rdd = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 3, 4, 5, 6, 7), 3)
    println(rdd.distinct().collect().mkString(","))
  }

  def sortBy(sc: SparkContext, log: RDD[Int]): Unit = {
    val rdd = sc.makeRDD(List(23, 14, 63, 52, 12, 22, 35, 80), 2)
    println(rdd.sortBy(num => num, false).collect().mkString(","))
  }

  def sortBy_string(sc: SparkContext, log: RDD[String]): Unit = {
    val rdd = sc.makeRDD(List("Hello", "Apache", "Hadoop", "Spark", "Java", "Python", "Go", "C++"), 2)
    println(rdd.sortBy(num => num.charAt(0), false).collect().mkString(","))
  }

  def sortBy_test(sc: SparkContext, log: RDD[String]): Unit = {
    val rdd = sc.makeRDD(List(("1", 1), ("11", 2), ("2", 3)), 2)
    println(rdd.sortBy(e => e._1.toInt, false).collect().mkString(","))
  }
}
