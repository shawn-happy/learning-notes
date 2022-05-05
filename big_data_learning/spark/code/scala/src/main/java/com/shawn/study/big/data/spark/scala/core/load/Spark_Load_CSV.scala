package com.shawn.study.big.data.spark.scala.core.load

import org.apache.spark.{SparkConf, SparkContext}
import au.com.bytecode.opencsv.CSVReader
import com.shawn.study.big.data.spark.scala.core.Person

import java.io.StringReader


object Spark_Load_CSV {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Spark Load CSV File Formatter")
    val sc = new SparkContext(conf)
    println(sc.textFile("../../data/rdd/load/person.csv")
      .map(line => {
        val reader = new CSVReader(new StringReader(line))
        reader.readNext()
      })
      .map(record => Person(record(0), Integer.valueOf(record(1))))
      .collect().mkString(","))
    sc.stop()
  }

}
