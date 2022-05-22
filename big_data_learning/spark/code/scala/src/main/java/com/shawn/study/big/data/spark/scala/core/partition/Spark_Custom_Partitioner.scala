package com.shawn.study.big.data.spark.scala.core.partition

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext, TaskContext}

object Spark_Custom_Partitioner {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("partitioner").setMaster("local")
    val sc = new SparkContext(conf)
    val list = List("beijing-1", "beijing-2", "beijing-3", "shanghai-1", "shanghai-2", "tianjing-1", "tianjing-2");

    val rdd: RDD[String] = sc.parallelize(list)
    rdd.cache()
    rdd.persist()

    rdd.map((_, 1)).partitionBy(new CustomPartitioner(3)).foreachPartition(t => {
      val id = TaskContext.get.partitionId
      println("partitionNum:" + id)
      t.foreach(data => {
        println(data)
      })
    })
    sc.stop()

  }

}

class CustomPartitioner(numParts: Int) extends Partitioner {
  override def numPartitions: Int = numParts

  //覆盖分区号获取函数
  override def getPartition(key: Any): Int = {
    //以"-"分割数据，将前缀相同的数据放在一个分区
    val prex = key.toString.split("-").apply(0)
    val code = (prex.hashCode % numPartitions)
    if (code < 0) {
      code + numPartitions
    } else {
      code
    }
  }
}
