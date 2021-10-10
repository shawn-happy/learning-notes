from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="coalesce-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize(c=[1, 1, 2, 2, 3, 3, 4, 4], numSlices=3)
    print(rdd.coalesce(2))
    context.stop()
