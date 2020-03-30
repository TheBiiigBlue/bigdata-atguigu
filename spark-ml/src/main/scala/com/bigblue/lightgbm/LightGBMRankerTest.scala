package com.bigblue.lightgbm

import com.microsoft.ml.spark.lightgbm.{LightGBMClassifier, LightGBMRanker, LightGBMRankerModel}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * Created By TheBigBlue on 2020/3/4
 * Description :
 */
object LightGBMRankerTest {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().appName("test-lightgbm").master("local[2]").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    val originalData: DataFrame = spark.read.option("header", "true") //第一行作为Schema
      .option("inferSchema", "true") //推测schema类型
      //      .csv("/home/hdfs/hour.csv")
      .csv("file:///D:/Cache/ProgramCache/TestData/dataSource/lightgbm/hour.csv")

    val labelCol = "workingday"
    //离散列
    val cateCols = Array[String]("season", "yr", "mnth", "hr")
    // 连续列
    val conCols: Array[String] = Array("temp", "atemp", "hum", "casual", "cnt")
    //feature列
    val vecCols = conCols ++ cateCols

    import spark.implicits._
    var inputDF = originalData.select(labelCol, vecCols: _*)
    vecCols.foreach(col => {
      inputDF = inputDF.withColumn(col, $"$col".cast(DoubleType))
    })
    inputDF = inputDF.withColumn(labelCol, $"$labelCol".cast(IntegerType))

    //追加一列index列作为groupCol,不指定groupCol报错
    import org.apache.spark.sql.functions._
    inputDF = inputDF.withColumn("index", monotonically_increasing_id)
//    val structType: StructType = inputDF.schema.add(StructField("index", LongType))
//    val zipRDD: RDD[Row] = inputDF.rdd.zipWithIndex().map(tp => Row.merge(tp._1, Row(tp._2)))
//    val fitDF = spark.createDataFrame(zipRDD, structType)
    inputDF.show

    val assembler = new VectorAssembler().setInputCols(vecCols).setOutputCol("features")

    val classifier: LightGBMRanker = new LightGBMRanker().setNumIterations(100).setNumLeaves(31)
      .setBoostFromAverage(false).setFeatureFraction(1.0).setMaxDepth(-1).setMaxBin(255)
      .setLearningRate(0.1).setMinSumHessianInLeaf(0.001).setLambdaL1(0.0).setLambdaL2(0.0)
      .setBaggingFraction(1.0).setBaggingFreq(0).setBaggingSeed(1).setObjective("lambdarank")
      .setLabelCol(labelCol).setCategoricalSlotNames(cateCols).setFeaturesCol("features")
      .setGroupCol("index").setBoostingType("gbdt")

    val pipelineModel = new Pipeline().setStages(Array(assembler, classifier)).fit(inputDF)
    val rankerModel = pipelineModel.stages(1).asInstanceOf[LightGBMRankerModel]
    val importanceValues = rankerModel.getFeatureImportances("split")
    //排序取前百分之
    val filteredTuples = vecCols.zip(importanceValues).sortWith(_._2 > _._2)
          .take((0.6 * vecCols.size).intValue())
    //生成重要性df
    var index = 0
    val importanceRDD: Array[LightGBMRankerTest] = filteredTuples.map(tuple => {
      index += 1
      LightGBMRankerTest(index, tuple._1, tuple._2)
    })
    val importanceDF = spark.createDataFrame(importanceRDD)
    importanceDF.show
    val filteredCols: Array[String] = filteredTuples.map(_._1)
    val finalDF = inputDF.select(labelCol, filteredCols: _*)
    finalDF.show
  }
}

case class LightGBMRankerTest(id: Long, feature_name: String, value: Double)
