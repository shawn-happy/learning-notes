package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object DoubleValueDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("double-value-demo")
    val sc = new SparkContext(sparkConf)

    val rdd1 = sc.makeRDD(List(1, 2, 3, 4))
    val rdd2 = sc.makeRDD(List(3, 4, 5, 6))
    val rdd7 = sc.makeRDD(List("3", "4", "5", "6"))

    // 交集 : 【3，4】
    val rdd3: RDD[Int] = rdd1.intersection(rdd2)
    //val rdd8 = rdd1.intersection(rdd7)
    println(rdd3.collect().mkString(","))

    // 并集 : 【1，2，3，4，3，4，5，6】
    val rdd4: RDD[Int] = rdd1.union(rdd2)
    println(rdd4.collect().mkString(","))

    // 差集 : 【1，2】
    val rdd5: RDD[Int] = rdd1.subtract(rdd2)
    println(rdd5.collect().mkString(","))

    // 拉链 : 【1-3，2-4，3-5，4-6】
    val rdd6: RDD[(Int, Int)] = rdd1.zip(rdd2)
    val rdd8 = rdd1.zip(rdd7)
    println(rdd6.collect().mkString(","))
    println(rdd8.collect().mkString(","))

    sc.stop()
  }

}
