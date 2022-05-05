package com.shawn.study.big.data.spark.scala.core.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark_Pair_Action_Operator {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val sc = new SparkContext(sparkConf)

    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)

    // aggregateByKey : 初始值只会参与分区内计算
    // aggregate : 初始值会参与分区内计算,并且和参与分区间计算
    //val result = rdd.aggregate(10)(_+_, _+_)
    val result = rdd.fold(10)(_ + _)

    println(result)


    val rdd2 = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3)
    ))

    //val intToLong: collection.Map[Int, Long] = rdd.countByValue()
    //println(intToLong)
    val stringToLong: collection.Map[String, Long] = rdd2.countByKey()
    println(stringToLong)

    sc.stop()

  }
}
