package com.shawn.study.big.data.spark.scala.core.persistence

import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel

object Spark_Persistence_Demo {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "Spark Persistence Demo")
    sc.makeRDD(List(1, 2, 3, 4)).cache()
    sc.makeRDD(List(1, 2, 3, 4)).persist()
    sc.makeRDD(List(1, 2, 3, 4)).persist(StorageLevel.DISK_ONLY)
    sc.makeRDD(List(1, 2, 3, 4)).unpersist()

  }

}
