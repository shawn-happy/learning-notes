from pyspark import SparkConf, SparkContext

data = list(['Hello World', 'Hello Spark', 'Hello Hadoop', 'Spark Learning'])


# wordCount by reduceByKey
def word_count_by_reduce_by_key(spark_context: SparkContext):
    print("word_count_by_reduce_by_key: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda line: line.split(" "))
          .map(lambda word: (word, 1))
          .reduceByKey(lambda a, b: a + b)
          .collect())


# wordCount by groupBy
def word_count_by_group_by(spark_context: SparkContext):
    print("word_count_by_group_by: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda word: word.split(" "))
          .groupBy(lambda word: word)
          .map(lambda x: (x[0], len(list(x[1]))))
          .collect())


# word count by group by key
def word_count_by_group_by_key(spark_context: SparkContext):
    print("word_count_by_group_by_key: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda word: word.split(" "))
          .map(lambda word: (word, 1))
          .groupByKey()
          .mapValues(lambda x: len(x))
          .collect())


# word count by aggregate_key
def word_count_by_aggregate_key(spark_context: SparkContext):
    print("word_count_by_aggregate_key: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda word: word.split(" "))
          .map(lambda word: (word, 1))
          .aggregateByKey(0, lambda k1, k2: k1 + k2, lambda v1, v2: v1 + v2)
          .collect())


def word_count_by_fold_key(spark_context: SparkContext):
    print("word_count_by_fold_key: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda word: word.split(" "))
          .map(lambda word: (word, 1))
          .foldByKey(0, lambda v1, v2: v1 + v2)
          .collect())


def word_count_by_combine_key(spark_context: SparkContext):
    print("word_count_by_combine_key: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda word: word.split(" "))
          .map(lambda word: (word, 1))
          .combineByKey(lambda v: v, lambda x, y: x + y, lambda x, y: x + y)
          .collect())


def word_count_by_count_key(spark_context: SparkContext):
    print("word_count_by_count_key: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda word: word.split(" "))
          .map(lambda word: (word, 1))
          .countByKey())


def word_count_by_count_by_value(spark_context: SparkContext):
    print("word_count_by_count_by_value: ")
    print(spark_context.parallelize(data)
          .flatMap(lambda word: word.split(" "))
          .countByValue())


if __name__ == '__main__':
    conf = SparkConf().setMaster("local").setAppName('spark-learning')
    sc = SparkContext(conf=conf)

    word_count_by_reduce_by_key(spark_context=sc)
    word_count_by_group_by(spark_context=sc)
    word_count_by_group_by_key(spark_context=sc)
    word_count_by_aggregate_key(spark_context=sc)
    word_count_by_fold_key(spark_context=sc)
    word_count_by_combine_key(spark_context=sc)
    word_count_by_count_key(spark_context=sc)
    word_count_by_count_by_value(spark_context=sc)

    # done!
    sc.stop()
