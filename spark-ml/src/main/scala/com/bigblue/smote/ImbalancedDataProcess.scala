package com.bigblue.smote

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * Created By TheBigBlue on 2020/3/23
 * Description : 
 */
object ImbalancedDataProcess {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("test-lightgbm").master("local[4]").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    val originalData: DataFrame = spark.read.option("header", "true") //第一行作为Schema
      .option("inferSchema", "true") //推测schema类型
      //      .csv("/home/hdfs/hour.csv")
      .csv("file:///F:\\Cache\\Program\\TestData\\lightgbm/hour.csv")

    val kNei = 5
    val nNei = 10
    //少数样本值
    val minSample = 0
    //标签列
    val labelCol = "workingday"
    // 连续列
    val vecCols: Array[String] = Array("temp", "atemp", "hum", "casual", "cnt")

    import spark.implicits._
    //原始数据只保留label和features列，追加一列sign标识为老数据
    val inputDF = originalData.select(labelCol, vecCols: _*).withColumn("sign", lit("O"))
    //需要对最小样本值的数据处理
    val filteredDF = inputDF.filter($"$labelCol" === minSample)
    //合并为label和向量列
    val labelAndVecDF = new VectorAssembler().setInputCols(vecCols).setOutputCol("features")
      .transform(filteredDF).select(labelCol, "features")
    //转为rdd
    val inputRDD = labelAndVecDF.rdd.map(row => (row.get(0).toString.toLong, row.getAs[Vector](1)))
    println(inputRDD.count() + "\t" + new SimpleDateFormat("HH:mm:ss").format(new Date()))

    //smote算法
    val vecRDD: RDD[Vector] = SmoteSampler.generateSamples1(inputRDD, kNei, nNei)
    println(vecRDD.count() + "\t" + new SimpleDateFormat("HH:mm:ss").format(new Date()))

    //以下是公司要求的和之前数据合并
    //生成dataframe，将向量列展开，追加一列sign标识为新数据
    val vecDF: DataFrame = vecRDD.map(vec => (0, vec.toArray)).toDF(labelCol, "features")
    val newCols = (0 until vecCols.size).map(i => $"features".getItem(i).alias(vecCols(i)))
    //根据需求，新数据应该为样本量*n，当前测试数据label为0的样本量为5514，则会新增5514*10=55140
    val newDF = vecDF.select(($"$labelCol" +: newCols): _*).withColumn("sign", lit("N"))

    //和原数据合并
    val finalDF = inputDF.union(newDF)
    finalDF.show

    //import scala.collection.JavaConversions._
    //查看原数据
    val aggSeq: util.List[Row] = originalData.groupBy(labelCol).agg(count(labelCol).as("labelCount"))
      .collectAsList()
    println(aggSeq)

    //查看平衡后数据，根据需求，则最终合并后，label为0的样本为55140+5514=60654
    val aggSeq1: util.List[Row] = finalDF.groupBy(labelCol).agg(count(labelCol).as("labelCount"))
      .collectAsList()
    println(aggSeq1)
  }
}
