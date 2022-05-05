package com.shawn.study.big.data.spark.scala.core.load

import org.apache.spark.SparkContext
import org.apache.spark.rdd.JdbcRDD

import java.sql.{DriverManager, ResultSet}

object Spark_Load_JDBC {

  def main(args: Array[String]) {
    val sc = new SparkContext("local", "LoadSimpleJdbc")
    val data = new JdbcRDD(sc,
      createConnection, "SELECT * FROM panda WHERE ? <= id AND ID <= ?",
      lowerBound = 1, upperBound = 3, numPartitions = 2, mapRow = extractValues)
    println(data.collect().toList)
  }

  def createConnection() = {
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    DriverManager.getConnection("jdbc:mysql://localhost/test?user=holden");
  }

  def extractValues(r: ResultSet) = {
    (r.getInt(1), r.getString(2))
  }

}
