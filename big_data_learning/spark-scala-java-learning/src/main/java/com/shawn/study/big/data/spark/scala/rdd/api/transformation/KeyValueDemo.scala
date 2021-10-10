package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.{SparkConf, SparkContext}

object KeyValueDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("key-value-demo")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(Seq(
      ("a", 1)
      , ("a", 2)
      , ("a", 3)
      , ("b", 4)
    ))

    // [("a", 6), ("b", 4)]
    rdd.reduceByKey((x: Int, y: Int) => x + y).collect().foreach(println)

    // [("a", [1, 2, 3]), ("b", [4])]
    rdd.groupByKey().collect().foreach(println)

    rdd.aggregateByKey(1)((x, y) => math.max(x, y), (x, y) => x + y).collect.foreach(println)

    rdd.foldByKey(0)(_ + _).collect.foreach(println)
    sc.stop()
  }

}
