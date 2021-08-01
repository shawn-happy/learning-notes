from pyspark import SparkConf, SparkContext

if __name__ == '__main__':
    conf = SparkConf().setMaster("local").setAppName('spark-learning')
    sc = SparkContext(conf=conf)
    inputPath = "F:\\dev\\learning-notes\\big_data_learning\\spark-learning\\resources\\quickstart\\work-count"
    # create RDD from a text file
    baseRDD = sc.textFile(inputPath)
    print(baseRDD.collect())

    wordsRDD = baseRDD.flatMap(lambda line: line.split(" "))
    print(wordsRDD.collect())

    pairsRDD = wordsRDD.map(lambda word: (word, 1))
    print(pairsRDD.collect())

    frequenciesRDD = pairsRDD.reduceByKey(lambda a, b: a + b)
    print(frequenciesRDD.collect())

    # done!
    sc.stop()

