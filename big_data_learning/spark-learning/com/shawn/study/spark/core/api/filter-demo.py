from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="filter-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
    print(rdd.filter(lambda num: num % 2 == 1).collect())
    context.stop()
