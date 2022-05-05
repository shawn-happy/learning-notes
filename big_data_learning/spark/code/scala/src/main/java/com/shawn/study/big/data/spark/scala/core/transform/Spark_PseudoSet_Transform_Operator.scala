package com.shawn.study.big.data.spark.scala.core.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark_PseudoSet_Transform_Operator {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("rdd_operator")
    val sc = new SparkContext(conf)
    operate(sc)
    sc.stop()
  }

  def operate(sc: SparkContext): Unit = {
    val rdd1 = sc.makeRDD(List("coffee", "coffee", "panda", "monkey", "tea"), 2)
    val rdd2 = sc.makeRDD(List("coffee", "monkey", "kitty"), 2)
    val rdd3 = sc.makeRDD(List("panda", "tea", "dog"), 2)

    println("并集：")
    println(rdd1.union(rdd2).collect().mkString(","))
    println("交集：")
    println(rdd1.intersection(rdd2).collect().mkString(","))
    println("差集：")
    println(rdd1.subtract(rdd2).collect().mkString(","))
    println("笛卡尔积：")
    println(rdd1.cartesian(rdd2).collect().mkString(","))
    println("拉链：")
    println(rdd2.zip(rdd3).collect().mkString(","))
  }

}
