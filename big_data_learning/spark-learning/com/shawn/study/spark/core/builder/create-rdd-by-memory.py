from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="create-rdd-by-memory").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize([1, 2, 3])
    print(rdd.collect())
    context.stop()
