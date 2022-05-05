package com.shawn.study.big.data.spark.scala.core.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Two_Pair_RDD_Transform_Operator {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("rdd_two_pair_operator")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(List((1, 2), (3, 4), (3, 6)))
    val rdd2 = sc.makeRDD(List((3, 9)))
    twoPairOperator(sc, rdd1, rdd2)
    sc.stop()
  }

  def twoPairOperator(sc: SparkContext, rdd1: RDD[(Int, Int)], rdd2: RDD[(Int, Int)]): Unit = {
    println(rdd1.subtractByKey(rdd2).collect().mkString(","))
    println(rdd1.join(rdd2).collect().mkString(","))
    println(rdd1.leftOuterJoin(rdd2).collect().mkString(","))
    println(rdd1.rightOuterJoin(rdd2).collect().mkString(","))
    println(rdd1.cogroup(rdd2).collect().mkString(","))
  }

}
