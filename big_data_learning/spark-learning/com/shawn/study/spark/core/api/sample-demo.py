from pyspark import SparkConf, SparkContext
if __name__ == '__main__':
    conf = SparkConf().setAppName(value="sample-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
    print(rdd.sample(False, 0.8).collect())
    context.stop()