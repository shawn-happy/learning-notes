package com.shawn.study.big.data.spark.scala.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object WordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("wordCount");
    val sc = new SparkContext(conf)
    val path = "../../data/wc/word_count_example"
    val rdd = sc.textFile(path)
    wc1(rdd)
    wc2(rdd)
    wc3(rdd)
    wc4(rdd)
    wc5(rdd)
    wc6(rdd)
    wc7(rdd)
    wc8(rdd)
    wc9(rdd)
    wc10(rdd)
    sc.stop()
  }

  // flatMap + groupBy + map
  def wc1(rdd: RDD[String]): Unit = {
    rdd.flatMap(_.split(" ")).groupBy(word => word).map({
      case (word, list) =>
        (word, list.size)
    }).collect().foreach(println)
  }

  // flatMap + groupBy + mapValues
  def wc2(rdd: RDD[String]): Unit = {
    val group = rdd.flatMap(_.split(" ")).groupBy(word => word)
    group.mapValues(iter => iter.size).collect().foreach(println)
  }

  // flatMap + map + groupByKey + mapValues
  def wc3(rdd: RDD[String]): Unit = {
    val map = rdd.flatMap(_.split(" ")).map((_, 1))
    val groupByKey = map.groupByKey()
    val mapValues = groupByKey.mapValues(iter => iter.size)
    mapValues.collect().foreach(println)
  }

  // flatMap + map + reduceByKey
  def wc4(rdd: RDD[String]): Unit = {
    val map = rdd.flatMap(_.split(" ")).map((_, 1))
    val reduceByKey = map.reduceByKey(_ + _)
    reduceByKey.collect().foreach(println)
  }

  // flatMap + map + aggregateByKey
  def wc5(rdd: RDD[String]): Unit = {
    val map = rdd.flatMap(_.split(" ")).map((_, 1))
    val aggregateByKey = map.aggregateByKey(0)(_ + _, _ + _)
    aggregateByKey.collect().foreach(println)
  }

  // flatMap + map + foldByKey
  def wc6(rdd: RDD[String]): Unit = {
    val map = rdd.flatMap(_.split(" ")).map((_, 1))
    val foldByKey = map.foldByKey(0)(_ + _)
    foldByKey.collect().foreach(println)
  }

  // flatMap + map + combineByKey
  def wc7(rdd: RDD[String]): Unit = {
    val map = rdd.flatMap(_.split(" ")).map((_, 1))
    val combineByKey = map.combineByKey(v => v, (x: Int, y) => x + y, (x: Int, y: Int) => x + y)
    combineByKey.collect().foreach(println)
  }

  // flatMap + map + countByKey
  def wc8(rdd: RDD[String]): Unit = {
    val map = rdd.flatMap(_.split(" ")).map((_, 1))
    val countByKey = map.countByKey()
    countByKey.keys.foreach(key => println("(" + key + ", " + countByKey(key) + ")"))
  }

  // flatMap + countByValue
  def wc9(rdd: RDD[String]): Unit = {
    val flatMap = rdd.flatMap(_.split(" "))
    val countByValue = flatMap.countByValue()
    countByValue.keys.foreach(key => println("(" + key + ", " + countByValue(key) + ")"))
  }

  // flatMap + map + reduce
  def wc10(rdd: RDD[String]): Unit = {
    val map = rdd.flatMap(_.split(" ")).map(word => {
      mutable.Map[String, Long]((word, 1))
    })
    val value = map.reduce((map1, map2) => {
      map2.foreach {
        case (word, count) =>
          val newCount = map1.getOrElse(word, 0L) + count
          map1.update(word, newCount)
      }
      map1
    })
    println(value)
  }
}
