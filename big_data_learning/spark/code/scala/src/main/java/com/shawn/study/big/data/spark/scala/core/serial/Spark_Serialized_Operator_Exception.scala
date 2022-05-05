package com.shawn.study.big.data.spark.scala.core.serial

import org.apache.spark.SparkContext

object Spark_Serialized_Operator_Exception {

  // Task not serializable
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "spark serialized")
    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    val user = new User()
    rdd.foreach(num => {
      // java.io.NotSerializableException: com.shawn.study.big.data.spark.scala.core.serial.Spark_Serialized_Operator$User
      print("user age: \n" + (user.age + num))
    })
    sc.stop()
  }

  class User {
    val age: Int = 20
  }

}
