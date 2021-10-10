from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="sort-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize(c=[5, 3, 6, 4, 1, 2], numSlices=3)
    print(rdd.sortBy(keyfunc=lambda num: num).collect())
    context.stop()
