package com.shawn.study.big.data.spark.scala.rdd.api.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object KeyValueDemo {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("key-value-demo")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.makeRDD(Seq(
      ("a", 1)
      , ("a", 2)
      , ("a", 3)
      , ("b", 4)
    ))

    // [("a", 6), ("b", 4)]
    // reduceByKey : 相同的key的数据进行value数据的聚合操作
    // scala语言中一般的聚合操作都是两两聚合，spark基于scala开发的，所以它的聚合也是两两聚合
    // 【1，2，3】
    // 【3，3】
    // 【6】
    // reduceByKey中如果key的数据只有一个，是不会参与运算的。
    rdd.reduceByKey((x: Int, y: Int) => x + y).collect().foreach(println)

    // [("a", [1, 2, 3]), ("b", [4])]
    // groupByKey : 将数据源中的数据，相同key的数据分在一个组中，形成一个对偶元组
    //              元组中的第一个元素就是key，
    //              元组中的第二个元素就是相同key的value的集合
    rdd.groupByKey().collect().foreach(println)


    // aggregateByKey存在函数柯里化，有两个参数列表
    // 第一个参数列表,需要传递一个参数，表示为初始值
    //       主要用于当碰见第一个key的时候，和value进行分区内计算
    // 第二个参数列表需要传递2个参数
    //      第一个参数表示分区内计算规则
    //      第二个参数表示分区间计算规则

    // math.min(x, y)
    // math.max(x, y)
    rdd.aggregateByKey(0)((x, y) => math.max(x, y), (x, y) => x + y).collect.foreach(println)

    // 如果聚合计算时，分区内和分区间计算规则相同，spark提供了简化的方法
    rdd.foldByKey(0)(_ + _).collect.foreach(println)

    val rdd2 = sc.makeRDD(Seq(
      ("a", 1)
      , ("b", 4)
      , ("a", 2)
      , ("b", 4)
      , ("a", 3)
      , ("b", 6)
    ), 2)

    // aggregateByKey最终的返回数据结果应该和初始值的类型保持一致
    //val aggRDD: RDD[(String, String)] = rdd.aggregateByKey("")(_ + _, _ + _)
    //aggRDD.collect.foreach(println)

    // 获取相同key的数据的平均值 => (a, 3),(b, 4)
    val newRDD: RDD[(String, (Int, Int))] = rdd2.aggregateByKey((0, 0))(
      (t, v) => {
        (t._1 + v, t._2 + 1)
      },
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )

    val resultRDD: RDD[(String, Int)] = newRDD.mapValues {
      case (num, cnt) => {
        num / cnt
      }
    }
    resultRDD.collect().foreach(println)

    // combineByKey : 方法需要三个参数
    // 第一个参数表示：将相同key的第一个数据进行结构的转换，实现操作
    // 第二个参数表示：分区内的计算规则
    // 第三个参数表示：分区间的计算规则
    val combineRDD: RDD[(String, (Int, Int))] = rdd.combineByKey(
      v => (v, 1),
      (t: (Int, Int), v) => {
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )

    val resultCombineRDD: RDD[(String, Int)] = combineRDD.mapValues {
      case (num, cnt) => {
        num / cnt
      }
    }
    resultCombineRDD.collect().foreach(println)

    /*
      reduceByKey:

           combineByKeyWithClassTag[V](
               (v: V) => v, // 第一个值不会参与计算
               func, // 分区内计算规则
               func, // 分区间计算规则
               )

      aggregateByKey :

          combineByKeyWithClassTag[U](
              (v: V) => cleanedSeqOp(createZero(), v), // 初始值和第一个key的value值进行的分区内数据操作
              cleanedSeqOp, // 分区内计算规则
              combOp,       // 分区间计算规则
              )

      foldByKey:

          combineByKeyWithClassTag[V](
              (v: V) => cleanedFunc(createZero(), v), // 初始值和第一个key的value值进行的分区内数据操作
              cleanedFunc,  // 分区内计算规则
              cleanedFunc,  // 分区间计算规则
              )

      combineByKey :

          combineByKeyWithClassTag(
              createCombiner,  // 相同key的第一条数据进行的处理函数
              mergeValue,      // 表示分区内数据的处理函数
              mergeCombiners,  // 表示分区间数据的处理函数
              )

     */


    rdd2.reduceByKey(_ + _) // wordcount
    rdd2.aggregateByKey(0)(_ + _, _ + _) // wordcount
    rdd2.foldByKey(0)(_ + _) // wordcount
    rdd2.combineByKey(v => v, (x: Int, y) => x + y, (x: Int, y: Int) => x + y) // wordcount

    val rdd3 = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("c", 3)
    ))

    val rdd4 = sc.makeRDD(List(
      ("a", 5), ("c", 6), ("a", 4)
    ))

    // join : 两个不同数据源的数据，相同的key的value会连接在一起，形成元组
    //        如果两个数据源中key没有匹配上，那么数据不会出现在结果中
    //        如果两个数据源中key有多个相同的，会依次匹配，可能会出现笛卡尔乘积，数据量会几何性增长，会导致性能降低。
    val joinRDD: RDD[(String, (Int, Int))] = rdd3.join(rdd4)

    joinRDD.collect().foreach(println)

    //val leftJoinRDD = rdd1.leftOuterJoin(rdd2)
    val rightJoinRDD = rdd3.rightOuterJoin(rdd4)

    //leftJoinRDD.collect().foreach(println)
    rightJoinRDD.collect().foreach(println)

    sc.stop()
  }

}
