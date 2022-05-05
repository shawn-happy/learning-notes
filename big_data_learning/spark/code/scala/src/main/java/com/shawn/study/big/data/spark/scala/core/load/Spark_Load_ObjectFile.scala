package com.shawn.study.big.data.spark.scala.core.load

import org.apache.spark.{SparkConf, SparkContext}

object Spark_Load_ObjectFile {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Spark Load Object File Formatter")
    val sc = new SparkContext(conf)
    sc.objectFile("../../data/rdd/load/person").collect()
    sc.stop()
  }

}
