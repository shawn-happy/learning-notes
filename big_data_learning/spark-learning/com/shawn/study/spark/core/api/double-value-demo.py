from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="double-value-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)

    rdd1 = context.parallelize(c=[1, 2, 3, 4])
    rdd2 = context.parallelize(c=[3, 4, 5, 6])
    print(rdd1.intersection(rdd2).collect())
    print(rdd1.union(rdd2).collect())
    print(rdd1.subtract(rdd2).collect())
    print(rdd1.zip(rdd2).collect())
    context.stop()
