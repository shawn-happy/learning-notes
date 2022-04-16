from pyspark import SparkContext, SparkConf

if __name__ == "__main__":
    master = "local"
    app_name = "wordCount"
    path = "../../data/wc/word_count_example"
    conf = SparkConf().setMaster(master).setAppName(app_name)
    sc = SparkContext(conf=conf)
    result = sc.textFile(path).flatMap(lambda str: str.split(" ")).countByValue()
    for key, value in result.items():
        msg = f"{key}, {value}"
        print(msg)
    sc.stop
