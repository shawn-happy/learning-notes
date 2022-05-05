package com.shawn.study.big.data.spark.scala.core.load

import org.apache.spark.{SparkConf, SparkContext}

object Spark_Load_Memory {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("rdd_creator")
    val sc = new SparkContext(conf)
    val seq = Seq[Int](1, 2, 3, 4)
    //    parallelize(sc, seq, "parallelize")
    makeRDD(sc, seq, "makeRDD")
    makeRDDWithPartition(sc, seq, "makeRDD")
    sc.stop()
  }

  def parallelize(sc: SparkContext, seq: Seq[Int], msg: String): Unit = {
    println(msg + ":")
    sc.parallelize(seq).collect().foreach(println)
  }

  def makeRDD(sc: SparkContext, seq: Seq[Int], msg: String): Unit = {
    println(msg + ":")
    sc.makeRDD(seq).saveAsTextFile("../../output/spark/core/Spark_Create_RDD_From_Memory")
  }

  def makeRDDWithPartition(sc: SparkContext, seq: Seq[Int], msg: String): Unit = {
    println(msg + ":")
    sc.makeRDD(seq, 2).saveAsTextFile("../../output/spark/core/Spark_Create_RDD_From_Memory_With_Partition")
  }

  def makRDDWithConf(): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("rdd_creator").set("spark.default.parallelism", "5")
    val sc = new SparkContext(conf)
    val seq = Seq[Int](1, 2, 3, 4)
    sc.makeRDD(seq).saveAsTextFile("../../output/spark/core/Spark_Create_RDD_From_Memory_With_Default_Parallelism")
    sc.stop()
  }

}
