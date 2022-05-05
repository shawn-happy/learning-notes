package com.shawn.study.big.data.spark.scala.core.example

import org.apache.spark.{SparkConf, SparkContext}

object HotCategoryTop10 {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("hot_category_top10")
    val sc = new SparkContext(conf)
    val path = "../../data/example/01/user_visit_action.csv"
    val data = sc.textFile(path)
    data.flatMap(s => s.split(","))
    data.collect().foreach(println)
    sc.stop()
  }

}
