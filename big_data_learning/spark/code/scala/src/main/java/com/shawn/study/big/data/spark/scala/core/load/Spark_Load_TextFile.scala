package com.shawn.study.big.data.spark.scala.core.load

import org.apache.spark.{SparkConf, SparkContext}

object Spark_Load_TextFile {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(sparkConf)

    // 从文件中创建RDD，将文件中的数据作为处理的数据源
    // path路径默认以当前环境的根路径为基准。可以写绝对路径，也可以写相对路径
    //sc.textFile("D:\\mineworkspace\\idea\\classes\\atguigu-classes\\datas\\1.txt")
    //val rdd: RDD[String] = sc.textFile("datas/1.txt")
    // path路径可以是文件的具体路径，也可以目录名称
    //val rdd = sc.textFile("datas")
    // path路径还可以使用通配符 *
    //val rdd = sc.textFile("datas/1*.txt")
    // path还可以是分布式存储系统路径：HDFS
//    val rdd = sc.textFile("hdfs://linux1:8020/test.txt")
    val rdd = sc.wholeTextFiles("../../data/agent.txt")
    rdd.collect().foreach(println)

    sc.stop()
  }
}
