package com.shawn.study.big.data.spark.scala.core.save

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.shawn.study.big.data.spark.scala.core.Person
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Save_JSON {

  def main(args: Array[String]): Unit = {
    val shawn = Person("Shawn", 26)
    val jack = Person("Jack", 27)
    val jackson = Person("Jackson", 28)
    val john = Person("John", 22)
    val persons = List(shawn, jack, jackson, john)

    val conf = new SparkConf().setMaster("local[2]").setAppName("Spark Save JSON File Formatter")
    val sc = new SparkContext(conf)
    sc.makeRDD(persons).mapPartitions(records => {
      val mapper = new ObjectMapper with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      records.map(mapper.writeValueAsString(_))
    }).saveAsTextFile("../../output/rdd/save/person.json")
    sc.stop()
  }

}
