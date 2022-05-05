package com.shawn.study.big.data.spark.scala.core.load

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.shawn.study.big.data.spark.scala.core.Person
import org.apache.spark.{SparkConf, SparkContext}

object Spark_Load_Json {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Spark Load Json")
    val sc = new SparkContext(conf)

    val input = sc.textFile("../../data/rdd/load/person.json")
    println(input.mapPartitions(records => {
      val mapper = new ObjectMapper with ScalaObjectMapper
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      mapper.registerModule(DefaultScalaModule)
      records.flatMap(record => {
        try {
          Some(mapper.readValue(record, classOf[Person]))
        } catch {
          case e: Exception => None
        }
      })
    }, true).mapPartitions(records => {
      val mapper = new ObjectMapper with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      records.map(mapper.writeValueAsString(_))
    }).collect().mkString(","))
  }

}
