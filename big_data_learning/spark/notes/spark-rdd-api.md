### RDD Lazy Load

spark中的所有transformation操作都是懒加载，即执行transformation操作不会立刻执行，而是先记录RDD之间的转换关系，仅当Action触发时才会执行RDD的转换操作并将结果返回给驱动程序。

这种懒加载的设计是spark能够更好地对任务进行优化，避免不必要的计算，从而使得Spark运行更加合理高效。

### RDD Create API

#### 内存里创建RDD

> in java

```java
SparkConf conf = new SparkConf().setAppName("create-rdd-by-memory").setMaster("local[*]");
JavaSparkContext context = new JavaSparkContext(conf);
JavaRDD<String> rdd = context.parallelize(Arrays.asList("1", "2", "3"));
rdd.collect().forEach(System.out::println);
context.close();
```

> in scala

```scala
val sc = new SparkContext("local[*]", "create-rdd-by-memory")
val rdd = sc.makeRDD(Seq[Int](1, 2, 3))
rdd.collect().foreach(println)
sc.stop()
```

> in python

```python
conf = SparkConf().setAppName(value="create-rdd-by-memory").setMaster(value="local[*]")
context = SparkContext(conf=conf)
rdd = context.parallelize([1, 2, 3])
print(rdd.collect())
context.stop()
```

#### Create By Local File

> in java

```java
SparkConf conf = new SparkConf().setAppName("create-rdd-by-local-file").setMaster("local[*]");
JavaSparkContext context = new JavaSparkContext(conf);
JavaRDD<String> rdd = context.textFile("data/simple-data/1.txt");
rdd.collect().forEach(System.out::println);
context.close();
```

> in scala

```scala
val sc = new SparkContext("local[*]", "create-rdd-by-local-file")
val rdd = sc.textFile("data/simple-data/1.txt")
rdd.collect().foreach(println)
sc.stop()
```

> in python

```python
conf = SparkConf().setAppName(value="create-rdd-by-memory").setMaster(value="local[*]")
context = SparkContext(conf=conf)
rdd = context.textFile("data/simple-data/1.txt")
print(rdd.collect())
context.stop()
```

### RDD Transformation API

RDD 根据数据处理方式的不同将算子整体上分为 Value 类型、双 Value 类型和 Key-Value类型

#### Value类型

##### map

将处理的数据逐条进行映射转换。

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
List<Integer> list = javaRDD.map(num -> num * 2).collect();
```

> in scala

```scala
val rdd = sc.makeRDD(Seq[Int](1, 2, 3, 4, 5, 6, 7, 8))
rdd.map(num => num * 2).foreach(println)
```

> in python

```python
rdd = context.parallelize([1, 2, 3, 4, 5, 6, 7, 8])
print(rdd.map(lambda num: num * 2).collect())
```

##### mapPartitions

将待处理的数据以分区为单位发送到计算节点进行处理。

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2);
List<Integer> list = javaRDD.mapPartitions(iterator -> {
  int max = 0;
  while (iterator.hasNext()) {
    Integer next = iterator.next();
    max = Math.max(max, next);
  }
  return Collections.singletonList(max).iterator();
}).collect();
```

> in scala

```scala
val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)
val mpRDD = rdd.mapPartitions(
  iter => {
    List(iter.max).iterator
  }
)
mpRDD.collect().foreach(println)
```

> in python

```python
rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=2)
print(rdd.mapPartitions(f=get_max).collect())
```

 **map和mapPartitions的区别**

* 数据处理角度：
  * map是分区内一个一个数据执行
  * mapPartitions是以分区为单位进行批处理。
* 功能角度：
  * Map 算子主要目的将数据源中的数据进行转换和改变。但是不会减少或增多数据。
  * MapPartitions 算子需要传递一个迭代器，返回一个迭代器，没有要求的元素的个数保持不变，所以可以增加或减少数据。
* 性能角度：
  * Map 算子因为类似于串行操作，所以性能比较低
  * mapPartitions 算子类似于批处理，所以性能较高。
  * 但是 mapPartitions 算子会长时间占用内存，那么这样会导致内存可能不够用，出现内存溢出的错误。所以在内存有限的情况下，不推荐使用。使用 map 操作。

##### mapPartitionsWithIndex

将待处理的数据以分区为单位发送到计算节点进行处理，这里的处理是指可以进行任意的处理，哪怕是过滤数据，在处理时同时可以获取当前分区索引。

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 3);
List<Integer> list = javaRDD.mapPartitionsWithIndex((index, iterator) -> {
  if (index == 1) {
    return iterator;
  } else {
    return Collections.emptyIterator();
  }
}, false).collect();
```

> in scala

```scala
val rdd = sc.makeRDD(List(1,2,3,4), 2)
val mpiRDD = rdd.mapPartitionsWithIndex(
  (index, iter) => {
    if ( index == 0 ) {
      iter
    } else {
      Nil.iterator
    }
  }
)
mpiRDD.collect().foreach(println)
```

> in python

```python
rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
print(rdd.mapPartitionsWithIndex(f=get_index).collect())
```

##### flatMap

将处理的数据进行扁平化后再进行映射处理，所以算子也称之为扁平映射

> in java

```java
JavaRDD<List<Integer>> javaRDD = context.parallelize(
    Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5), Arrays.asList(6, 7, 8)), 2);
List<Integer> list = javaRDD.flatMap(List::iterator).collect();
```

> in scala

```scala
val rdd: RDD[List[Int]] = sc.makeRDD(List(
  List(1, 2), List(3, 4)
))
val flatRDD: RDD[Int] = rdd.flatMap(
  list => {
    list
  }
)
flatRDD.collect().foreach(println)
```

> in python

```python
rdd = context.parallelize(c=[[1, 2, 3], [4, 5], [6, 7, 8]], numSlices=3)
print(rdd.flatMap(f=lambda data: data).collect())
```

##### glom

将同一个分区的数据直接转换为相同类型的内存数组进行处理，分区不变。

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2);
List<List<Integer>> list = javaRDD.glom().collect();
```

> in scala

```scala
val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)
val glomRDD: RDD[Array[Int]] = rdd.glom()
val maxRdd = glomRDD.map(array => {
    array.max
})
println(maxRdd.collect().sum)
```

> in python

```python
rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
print(rdd.glom().collect())
```

##### groupBy

将数据根据指定的规则进行分组, 分区默认不变，但是数据会被打乱重新组合，我们将这样的操作称之为 shuffle。极限情况下，数据可能被分在同一个分区中一个组的数据在一个分区中，但是并不是说一个分区中只有一个组

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2);
List<Tuple2<Integer, Iterable<Integer>>> list = javaRDD.groupBy(num -> num % 2)
    .collect();
```

> in scala

```scala
val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)
val groupRDD: RDD[(Int, Iterable[Int])] = rdd.groupBy(num => num % 2)
groupRDD.collect().foreach(println)
```

> in python

```python
rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
print(rdd.groupBy(f=lambda num: num % 2).collect())
```

##### filter

将数据根据指定的规则进行筛选过滤，符合规则的数据保留，不符合规则的数据丢弃。当数据进行筛选过滤后，分区不变，但是分区内的数据可能不均衡，生产环境下，可能会出现数据倾斜。

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(
    Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2);
JavaRDD<Integer> filterRdd = javaRDD.filter(num -> num % 2 == 1);
```

>in scala

```scala
val rdd = sc.makeRDD(List(1,2,3,4))
val filterRDD: RDD[Int] = rdd.filter(num=>num%2!=0)
```

> in python

```python
rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
print(rdd.filter(lambda num: num % 2 == 1).collect())
```

##### sample

根据指定的规则从数据集中抽取数据

抽取数据不放回（伯努利算法）

伯努利算法：又叫 0、1 分布。例如扔硬币，要么正面，要么反面。

具体实现：根据种子和随机算法算出一个数和第二个参数设置几率比较，小于第二个参数要，大于不要

第一个参数：抽取的数据是否放回，false：不放回

第二个参数：抽取的几率，范围在[0,1]之间,0：全不取；1：全取；

第三个参数：随机数种子

抽取数据放回（泊松算法）

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(
    Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2);
List<Integer> list = javaRDD.sample(false, 0.6).collect();
```

> in scala

```scala
val rdd = sc.makeRDD(List(1,2,3,4))
rdd.sample(true, 0.5).collect().foreach(println)
```

> in python

```python
rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
print(rdd.sample(False, 0.8).collect())
```

##### distinct

将数据集中重复的数据去重

> in java

```java
context.parallelize(
    Arrays.asList(1, 2, 1, 2, 3, 3, 4, 4), 2).distinct().collect()
```

> in scala

```scala
val rdd = sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4))
val rdd1: RDD[Int] = rdd.distinct()
```

> in python

```python
rdd = context.parallelize(c=[1, 1, 2, 2, 3, 3, 4, 4], numSlices=3)
print(rdd.distinct().collect())
```

##### sortBy

该操作用于排序数据。在排序之前，可以将数据通过 f 函数进行处理，之后按照 f 函数处理的结果进行排序，默认为升序排列。排序后新产生的 RDD 的分区数与原 RDD 的分区数一致。中间存在 shuffle 的过程。

> in java

```java
JavaRDD<Integer> javaRDD = context.parallelize(
    Arrays.asList(1, 6, 5, 3, 4, 7, 2, 8), 2);
final List<Integer> result = javaRDD.sortBy(num -> num, true, 2).collect();
```

> in scala

```scala
val rdd = sc.makeRDD(List(1, 6, 5, 3, 4, 7, 2, 8))
val sortRdd: RDD[Int] = rdd.sortBy(num => num)
```

> in python

```python
rdd = context.parallelize(c=[5, 3, 6, 4, 1, 2], numSlices=3)
print(rdd.sortBy(keyfunc=lambda num: num).collect())
```

#### 双value类型

##### intersection

交集

##### union

并集

##### subtract

差集

##### zip

将两个 RDD 中的元素，以键值对的形式进行合并。其中，键值对中的 Key 为第 1 个 RDD

中的元素，Value 为第 2 个 RDD 中的相同位置的元素。

> in java

```java
JavaRDD<Integer> rdd1 = context.parallelize(Arrays.asList(1, 2, 3, 4));
JavaRDD<Integer> rdd2 = context.parallelize(Arrays.asList(3, 4, 5, 6));
System.out.println(StringUtils.join(rdd1.intersection(rdd2).collect(), ", "));
System.out.println(StringUtils.join(rdd1.union(rdd2).collect(), ", "));
System.out.println(StringUtils.join(rdd1.subtract(rdd2).collect(), ", "));
System.out.println(StringUtils.join(rdd1.zip(rdd2).collect(), ", "));
```

> in scala

```scala
val rdd1 = sc.makeRDD(List(1, 2, 3, 4))
val rdd2 = sc.makeRDD(List(3, 4, 5, 6))
val rdd7 = sc.makeRDD(List("3", "4", "5", "6"))

// 交集 : 【3，4】
val rdd3: RDD[Int] = rdd1.intersection(rdd2)
//val rdd8 = rdd1.intersection(rdd7)
println(rdd3.collect().mkString(","))

// 并集 : 【1，2，3，4，3，4，5，6】
val rdd4: RDD[Int] = rdd1.union(rdd2)
println(rdd4.collect().mkString(","))

// 差集 : 【1，2】
val rdd5: RDD[Int] = rdd1.subtract(rdd2)
println(rdd5.collect().mkString(","))

// 拉链 : 【1-3，2-4，3-5，4-6】
val rdd6: RDD[(Int, Int)] = rdd1.zip(rdd2)
val rdd8 = rdd1.zip(rdd7)
println(rdd6.collect().mkString(","))
println(rdd8.collect().mkString(","))
```

> in python

```python
rdd1 = context.parallelize(c=[1, 2, 3, 4])
rdd2 = context.parallelize(c=[3, 4, 5, 6])
print(rdd1.intersection(rdd2).collect())
print(rdd1.union(rdd2).collect())
print(rdd1.subtract(rdd2).collect())
print(rdd1.zip(rdd2).collect())
```

#### key-value类型

##### reduceByKey

可以将数据按照相同的 Key 对 Value 进行聚合

##### groupByKey

将数据源的数据根据 key 对 value 进行分组



**从** **shuffle** **的角度**：reduceByKey 和 groupByKey 都存在 shuffle 的操作，但是 reduceByKey

可以在 shuffle 前对分区内相同 key 的数据进行预聚合（combine）功能，这样会减少落盘的

数据量，而 groupByKey 只是进行分组，不存在数据量减少的问题，reduceByKey 性能比较

高。

**从功能的角度**：reduceByKey 其实包含分组和聚合的功能。GroupByKey 只能分组，不能聚

合，所以在分组聚合的场合下，推荐使用 reduceByKey，如果仅仅是分组而不需要聚合。那

么还是只能使用 groupByKey

##### aggregateByKey

将数据根据不同的规则进行分区内计算和分区间计算，取出每个分区内相同 key 的最大值然后分区间相加

##### foldByKey

当分区内计算规则和分区间计算规则相同时，aggregateByKey 就可以简化为 foldByKey

##### combineByKey

最通用的对 key-value 型 rdd 进行聚集操作的聚集函数（aggregation function）。类似于

aggregate()，combineByKey()允许用户返回值的类型与输入不一致。

##### sortByKey

在一个(K,V)的 RDD 上调用，K 必须实现 Ordered 接口(特质)，返回一个按照 key 进行排序

的

##### join

在类型为(K,V)和(K,W)的 RDD 上调用，返回一个相同 key 对应的所有元素连接在一起的

(K,(V,W))的 RDD

##### leftOuterJoin

类似于 SQL 语句的左外连接

##### cogroup

在类型为(K,V)和(K,W)的 RDD 上调用，返回一个(K,(Iterable<V>,Iterable<W>))类型的 RDD



### RDD Action API

#### reduce

#### collect

#### count

#### first

#### take

#### takeOrdered

#### aggregate

#### fold

#### countByKey

#### saveXXX

#### foreach

### Spark分区器

#### 分区API



##### coalesce

根据数据量缩减分区，用于大数据集过滤后，提高小数据集的执行效率。当 spark 程序中，存在过多的小任务的时候，可以通过 coalesce 方法，收缩合并分区，减少分区的个数，减小任务调度成本



##### repartition

该操作内部其实执行的是 coalesce 操作，参数 shuffle 的默认值为 true。无论是将分区数多的RDD 转换为分区数少的 RDD，还是将分区数少的 RDD 转换为分区数多的 RDD，repartition操作都可以完成，因为无论如何都会经 shuffle 过程。



coalesce算子可以扩大分区，但是如果不进行shuffle操作，是没有意义，不起作用。所以如果想要实现扩大分区的效果，需要使用shuffle操作。

缩减分区：coalesce，如果想要数据均衡，可以采用shuffle。

扩大分区：repartition，底层代码调用的是coalesce，而且采用了shuffle。



### Spark序列化

### Spark广播变量

### Spark累加器

### RDD持久化，检查点等API其余示例

