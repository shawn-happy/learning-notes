package com.shawn.study.big.data.spark.java;

import org.apache.spark.api.java.JavaSparkContext;

public interface SparkContextAction<R> {

  R action(JavaSparkContext sparkContext);
}
