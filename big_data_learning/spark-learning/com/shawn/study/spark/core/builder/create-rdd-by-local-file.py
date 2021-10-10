from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    conf = SparkConf().setAppName(value="create-rdd-by-memory").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.textFile("data/simple-data/1.txt")
    print(rdd.collect())
    context.stop()