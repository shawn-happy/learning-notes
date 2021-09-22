### RDD

#### 什么是RDD

* RDD（Resilient Distributed Dataset）叫做**弹性分布式数据集**，**是Spark中最基本的数据抽象**，它代表一个不可变、可分区、里面的元素可并行计算的集合。

* RDD支持丰富的操作算子。
  * transformation APIs: RDD转换，构建RDD的血缘关系
  * Action APIs:触发RDD的计算并将获取计算结果
* RDD基于血缘关系(Lineage)实现故障重恢复
* RDD数据可缓存到内存中并在多个操作之间共享

#### RDD的特点

- 只读：不能修改，只能通过转换操作生成新的 RDD。
- 分布式：可以分布在多台机器上进行并行处理。
- 弹性：计算过程中内存不够时它会和磁盘进行数据交换。
- 基于内存：可以全部或部分缓存在内存中，在多次计算间重用。

#### RDD的属性

* A list of partitions。一组分片，数据集的基本组成单位。对于RDD来说，每个分片都会被一个计算任务处理，并决定并行计算的粒度。用户可以在创建RDD时指定RDD的分片个数，如果没有指定，那么就会采用默认值。默认值就是程序所分配到的CPU Core的数目。
* A function for computing each split：一个计算每个分区的函数。Spark中RDD的计算是以分片为单位的，每个RDD都会实现compute函数以达到这个目的。compute函数会对迭代器进行复合，不需要保存每次计算的结果。
* A list of dependencies on other RDDs：RDD之间的依赖关系。RDD的每次转换都会生成一个新的RDD，所以RDD之间就会形成类似于流水线一样的前后依赖关系。在部分分区数据丢失时，Spark可以通过这个依赖关系重新计算丢失的分区数据，而不是对RDD的所有分区进行重新计算。
* Optionally, a Partitioner for key-value RDDs (e.g. to say that the RDD is hash-partitioned)：一个Partitioner，即RDD的分片函数。当前Spark中实现了两种类型的分片函数，一个是基于哈希的HashPartitioner，另外一个是基于范围的RangePartitioner。只有对于于key-value的RDD，才会有Partitioner，非key-value的RDD的Parititioner的值是None。Partitioner函数不但决定了RDD本身的分片数量，也决定了parent RDD Shuffle输出时的分片数量。
* Optionally, a list of preferred locations to compute each split on (e.g. block locations for an HDFS file)：一个列表，存储存取每个Partition的优先位置（preferred location）。对于一个HDFS文件来说，这个列表保存的就是每个Partition所在的块的位置。按照“移动数据不如移动计算”的理念，Spark在进行任务调度的时候，会尽可能地将计算任务分配到其所要处理数据块的存储位置。

#### RDD Partition

RDD数据集逻辑上被划分为多个Partition，RDD计算任务在物理上被分配到多个Task

#### RDD partitioner

HashPartitioner

RangePartitioner

自定义

#### RDD Dependency

#### RDD 持久化

#### RDD 其余概念

#### RDD WordCount Code