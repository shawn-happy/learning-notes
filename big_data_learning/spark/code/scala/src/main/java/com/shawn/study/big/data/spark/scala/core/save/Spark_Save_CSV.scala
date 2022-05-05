package com.shawn.study.big.data.spark.scala.core.save

import au.com.bytecode.opencsv.CSVWriter
import com.shawn.study.big.data.spark.scala.core.Person
import org.apache.spark.{SparkConf, SparkContext}

import java.io.StringWriter

object Spark_Save_CSV {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Spark Load CSV File Formatter")
    val sc = new SparkContext(conf)
    sc.makeRDD(List(Person("Shawn", 23), Person("Jack", 24), Person("Jackson", 25), Person("Bob", 26), Person("John", 27)))
      .filter(person => person.age >= 25)
      .map(person => {
        person.name
      })
      .mapPartitions(people => {
        val stringWriter = new StringWriter()
        val csvWriter = new CSVWriter(stringWriter)
        csvWriter.writeNext(people.toArray)
        Iterator(stringWriter.toString)
      })
      .saveAsTextFile("../../output/rdd/save/person.csv")
    sc.stop()
  }

}
