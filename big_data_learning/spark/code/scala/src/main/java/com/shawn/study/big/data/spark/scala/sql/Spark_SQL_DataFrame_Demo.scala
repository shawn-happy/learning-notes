package com.shawn.study.big.data.spark.scala.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Spark_SQL_DataFrame_Demo {

  def main(args: Array[String]): Unit = {
    val basePath = "../../data/"
    val spark = SparkSession.builder()
      .config(new SparkConf()
        .setMaster("local")
        .setAppName("Spark SQL DataFrame Demo")
        .set("spark.default.parallelism", "3"))
      .getOrCreate()

    val df_json = spark.read.json(basePath + "people.json")
    println("json dataframe: " + df_json.rdd.getNumPartitions)

    df_json.show()

  }

}
