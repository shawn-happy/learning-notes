### spark简介

在大数据计算领域，Spark已经成为了越来越流行、越来越受欢迎的计算平台之一。Spark的功能涵盖了大数据领域的离线批处理、SQL类处理、流式/实时计算、机器学习、图计算等各种不同类型的计算操作，应用范围与前景非常广泛。

Spark是UC Berkeley AMPLab（美国加州大学伯克利分校的AMP实验室）开源的通用的分布式大数据计算引擎。它扩展了广泛使用的Map-Reduce计算模型。高效的支撑更多计算模式，包括交互式查询和流处理。spark的一个主要特点是能够在内存中进行计算，及时依赖磁盘进行复杂的运算，Spark依然比Map-Reduce更加高效。

Spark的分析引擎处理数据的速度比其他替代品快10到100倍。它通过跨大型计算机集群分布处理工作来扩展，具有内置的并行性和容错性。它甚至包括在数据分析师和数据科学家中流行的编程语言的API，包括Scala、Java、Python和R。

### Spark vs Map-Reduce

* 性能：

  Apache Spark在随机存取存储器(RAM) 中处理数据，而Hadoop Map-Reduce在执行映射或归约操作后将数据持久化回磁盘。从理论上讲，Spark应 该胜过Hadoop Map-Reduce。

  尽管如此，Spark需要大量内存。 与标准数据库非常相似，Spark将 进程加载到内存中，并将其保留在内存中，直到出于缓存目的而另行通知为止。如果您在具有其他资源需求服务的Hadoop YARN.上运行Spark，或者如果数据太大而无法完全容纳到内存中，则Spark可能会遭受严重的性能下降。

  另一方面，Map-Reduce会 在完成一项工作后立即终止其进程，因此它可以轻松地与其他服务一起运行，但性能会有细微的差别。

  对于需要多次传递相同数据的迭代计算，Spark具有优势。 但是，当涉及到类似ETL的一遍工作时(例如，数据转换或数据集成)， 这正是Map-Reduce设计的目的。

* 容错性：

  与Map-Reduce一样，Spark对每 个任务和推测性执行都有重试。但是，Map-Reduce在这里有一点优势，因为它依赖于硬盘驱动器而不是RAM。如果MapReduce进程在执行过程中崩溃，则它可以从中断处继续执行，而Spark必须从头开始处理。

  Spark和Hadoop Map-Reduce都具有良好的容错能力，但是Hadoop Map-Reduce的容错性稍强。

* 易用性：

  Spark具有针对Java，Scala和Python的预构建API， 并且还包括针对SQL精通的Spark SQL (以前称为Shark) 。得益于Spark的简单构建块，编写用户定义的函数很容易。Spark甚至包括一种交互式模式，用于运行具有即时反馈的命令。

  Map-Reduce是用Java编写的，因此很难编程。Apache Pig使它更容易(尽管需要一 些时间来学习语法)，而Apache Hive为该板增加了SQL兼容性。一些Hadoop具也可以运行Map-Reduce作业，而无需进行任何编程。例如，Xplenty 是一种基于Hadoop的数据集成服务，并且不需要任何编程或部署。

  Spark支持很多操作：

  * Transformation操作：`map, flatMap,filter,sample,groupByKey,reduceByKey,union,join,sort,partitionByKey`
  * Action操作：`collect,reduce,lookup,save等`
  * `shuffle`
  * `sql`

* 安全性：

  在安全性方面，与MapReduce相比，Spark不那么先进。 实际上，Spark中的安全性默认情况下设置为关闭，这会使您容易受到攻击。

  RPC通道通过共享密钥支持Spark中的身份验证。Spark包含 事件日志记录功能，并且可以通过javax servlet过滤器保护Web∪l。另外，由于Spark可以在YARN上运行并使用HDFS，因此它还可以享受Kerberos身份验证，HDFS文件权限以及节点之间的加密。

  Hadoop MapReduce可以享受Hadoop的所有安全优势，并且可以与Hadoop安全项目(例如Knox Gateway和Apache Sentry)集成。旨在提高Hadoop安全性的Rhino项目仅在增加Sentry支持方面提到了Spark。否则，Spark开 发人员必须自己提高Spark安全性。

  与具有更多安全功能和项目的MapReduce相比，Spark安全性尚不完善。

* 数据处理：

  Spark不仅可以执行简单的数据处理，还可以处理图形，并且包括MLlib机器学习库。Spark还可以执行实时处理以及批处理。处理的方式也很多，可以使用RDD，也可以使用DataFrame,DataSet等高级API，或者使用SQL来处理。

  Hadoop MapReduce非常适合批处理。如果需要实时选项，则需要使用其他平台，例如Impala或Apache Storm，对于图形处理，可以使用Apache Graph。MapReduce过去曾使用Apache Mahout进行机器学习，但此后却被Spark取代。

凭借其内存中数据处理功能，Spark具有 出色的性能并且具有很高的成本效益。它与Hadoop的所有数据源和文件格式兼容，并且学习曲线更快，并且具有适用于多种编程语言的友好API。Spark甚 至包括图形处理和机器学习功能。

Hadoop MapReduce是一个更成熟的平台，它是专门为批处理而构建的。对于无法容纳在内存中的超大数据，MapReduce 可能比Spark更具成本效益，并且可能更容易找到具有MapReduce经验的员工。此外，由于许多支持项目，工具和云服务，MapReduce生态系统目前更大。

### Spark生态介绍

* 支持多种资源调度的方式
  * Local（主要应用于开发模式）
  * Standalone（主要用于测试模式）
  * YARN
  * K8s
  * meSOS
* 支持多种数据格式
  * Json
  * CSV
  * TXT
  * Parquet
  * ORC
* 支持多种语言API是支持
  * Java
  * Scala
  * Python(PySpark)
  * R(SparkR)
* 支持多种存储介质
  * HDFS
  * HBase
  * Es
  * MySQL
  * Neo4J
  * Redis
  * MongoDB
  * Hive
  * Cassandra
* 支持多种数据源
  * Kafka
  * Flume
* 多个模块
  * Spark Core
  * Spark Streaming
  * Spark SQL
  * Spark ML LIb
  * Spark GraphX
  * Spark R

### 参考文献

[spark-vs-hadoop-mapreduce](https://www.xplenty.com/blog/apache-spark-vs-hadoop-mapreduce/)

[Spark-wiki](https://en.wikipedia.org/wiki/Apache_Spark)

