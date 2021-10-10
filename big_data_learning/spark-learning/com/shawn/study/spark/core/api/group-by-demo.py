from pyspark import SparkContext, SparkConf

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="group-by-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
    print(rdd.groupBy(f=lambda num: num % 2).collect())
    context.stop()