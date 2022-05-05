package com.shawn.study.big.data.spark.scala.core.acc

import org.apache.spark.{SparkConf, SparkContext}

object Spark_Acc_Operator {

  def main(args: Array[String]): Unit = {
    val sparConf = new SparkConf().setMaster("local").setAppName("Acc")
    val sc = new SparkContext(sparConf)

    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    val sumAcc = sc.longAccumulator("sum")
//    rdd.foreach(
//      num => {
//        // 使用累加器
//        sumAcc.add(num)
//      }
//    )

    val mapRDD = rdd.map(
      num => {
        // 使用累加器
        sumAcc.add(num)
        num
      }
    )

    mapRDD.collect()
    mapRDD.collect()
    println(sumAcc.value)

    sc.stop()

  }

}
