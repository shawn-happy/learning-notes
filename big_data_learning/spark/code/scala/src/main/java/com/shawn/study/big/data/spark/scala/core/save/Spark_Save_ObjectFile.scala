package com.shawn.study.big.data.spark.scala.core.save

import com.shawn.study.big.data.spark.scala.core.Person
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Save_ObjectFile {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Spark Save Object File")
    val sc = new SparkContext(conf)
    val person = Person("Shawn", 23)
    sc.makeRDD(List(person)).saveAsObjectFile("../../output/rdd/save/objectFile")
    sc.stop()
  }

}
