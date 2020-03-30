package com.bigblue.smote

import org.apache.spark.ml.linalg._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * Created By TheBigBlue on 2020/3/25
 * Description : 
 */
object SmoteTest {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .master("local[2]")
      .appName("smote example")
      .getOrCreate()

    val newSamples: RDD[Vector] = example1(spark)
    newSamples.collect().foreach(println)
    spark.stop()
  }

  private
  def example(spark: SparkSession) = {
    // $example on$
    val df = spark.createDataFrame(Seq(
      (0L, Vectors.dense(1, 2)),
      (1L, Vectors.dense(3, 4)),
      (2L, Vectors.dense(5, 6))
    )).toDF("id", "features")

    val k = 2
    val N = 3
    val data = df.rdd.map(r => (r.getLong(0), r.getAs[Vector](1)))
    val newSamples = SmoteSampler.generateSamples(data, k, N)
    newSamples
  }

  private
  def example1(spark: SparkSession) = {
    // $example on$
    val df = spark.createDataFrame(Seq(
      (0L, Vectors.dense(1, 2)),
      (0L, Vectors.dense(3, 4)),
      (0L, Vectors.dense(11, 12)),
      (1L, Vectors.dense(5, 6)),
      (1L, Vectors.dense(7, 8)),
      (1L, Vectors.dense(9, 10))
    )).toDF("id", "features")

    val k = 2
    val N = 3
    val data = df.rdd.map(r => (r.getLong(0), r.getAs[Vector](1)))
    val newSamples = SmoteSampler.generateSamples1(data, k, N)
    newSamples
  }
}
