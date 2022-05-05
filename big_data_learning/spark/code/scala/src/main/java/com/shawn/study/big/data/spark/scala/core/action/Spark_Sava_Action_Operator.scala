package com.shawn.study.big.data.spark.scala.core.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark_Sava_Action_Operator {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Save_Action_Operator")
    val sc = new SparkContext(sparkConf)

    //val rdd = sc.makeRDD(List(1,1,1,4),2)
    val rdd = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3)
    ))

    rdd.saveAsTextFile("../../output/spark/core/Spark_Save_Action_Operator_Text")
    rdd.saveAsObjectFile("../../output/spark/core/Spark_Save_Action_Operator_Object")
    // saveAsSequenceFile方法要求数据的格式必须为K-V类型
    rdd.saveAsSequenceFile("../../output/spark/core/Spark_Save_Action_Operator_KV")

    sc.stop()

  }
}
