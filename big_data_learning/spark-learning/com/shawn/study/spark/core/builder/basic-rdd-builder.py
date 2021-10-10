from pyspark import SparkConf, SparkContext
from tempfile import NamedTemporaryFile

data = list(['Hello World', 'Hello Spark', 'Hello Hadoop', 'Spark Learning'])
tempFile = NamedTemporaryFile(delete=True)
tempFile.close()


def build_in_memory_with_default_partition(spark_context: SparkContext):
    rdd = spark_context.parallelize(data)
    print(rdd.getNumPartitions())


def build_in_memory_with_custom_partition(spark_context: SparkContext, number: int):
    rdd = spark_context.parallelize(data, number)
    print(rdd.getNumPartitions())


if __name__ == '__main__':
    conf = SparkConf().setMaster("local[*]").setAppName('spark-learning')
    sc = SparkContext(conf=conf)
    print("default parallelism number :")
    print(sc.defaultParallelism)
    build_in_memory_with_default_partition(spark_context=sc)
    build_in_memory_with_custom_partition(spark_context=sc, number=20)

    sc.stop()
