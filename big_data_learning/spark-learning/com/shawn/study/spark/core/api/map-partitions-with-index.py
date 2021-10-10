from pyspark import SparkContext, SparkConf


def get_index(index, iterator):
    if index == 1:
        return iterator
    else:
        return []


if __name__ == '__main__':
    conf = SparkConf().setAppName(value="map-partition-with-index-demo").setMaster(value="local[*]")
    context = SparkContext(conf=conf)
    rdd = context.parallelize(c=[1, 2, 3, 4, 5, 6, 7, 8], numSlices=3)
    print(rdd.mapPartitionsWithIndex(f=get_index).collect())
    context.stop()
