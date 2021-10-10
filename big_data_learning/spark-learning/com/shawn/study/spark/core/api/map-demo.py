from pyspark import SparkContext, SparkConf

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="operator").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize([1, 2, 3, 4, 5, 6, 7, 8])
    print(rdd.map(lambda num: num * 2).collect())
    context.stop()