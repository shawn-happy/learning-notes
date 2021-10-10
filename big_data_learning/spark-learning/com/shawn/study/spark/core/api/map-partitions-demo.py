from pyspark import SparkContext, SparkConf


def get_max(iterator):
    return [max(iterator)]


if __name__ == '__main__':
    conf = SparkConf().setAppName(value="map-partition-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=2)
    print(rdd.mapPartitions(f=get_max).collect())
    context.stop()
