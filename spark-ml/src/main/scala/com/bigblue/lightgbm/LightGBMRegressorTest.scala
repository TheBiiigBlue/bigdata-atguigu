package com.bigblue.lightgbm

import com.bigblue.utils.LightGBMUtils
import com.microsoft.ml.spark.lightgbm.{LightGBMRegressionModel, LightGBMRegressor}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.types.{DoubleType, IntegerType}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Created By TheBigBlue on 2020/3/6
 * Description : 
 */
object LightGBMRegressorTest {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().appName("test-lightgbm").master("local[2]").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    var originalData: DataFrame = spark.read.option("header", "true") //第一行作为Schema
      .option("inferSchema", "true") //推测schema类型
      //      .csv("/home/hdfs/hour.csv")
      .csv("file:///D:/Cache/ProgramCache/TestData/dataSource/lightgbm/hour.csv")

    val labelCol = "workingday"
    //离散列
    val cateCols = Array("season", "yr", "mnth", "hr")
    // 连续列
    val conCols: Array[String] = Array("temp", "atemp", "hum", "casual", "cnt")
    //feature列
    val vecCols = conCols ++ cateCols

    import spark.implicits._
    vecCols.foreach(col => {
      originalData = originalData.withColumn(col, $"$col".cast(DoubleType))
    })
    originalData = originalData.withColumn(labelCol, $"$labelCol".cast(IntegerType))

    val assembler = new VectorAssembler().setInputCols(vecCols).setOutputCol("features")

    //    val classifier: LightGBMRegressor = new LightGBMRegressor().setNumIterations(100).setNumLeaves(31)
    //      .setBoostFromAverage(false).setFeatureFraction(1.0).setMaxDepth(-1).setMaxBin(255)
    //      .setLearningRate(0.1).setMinSumHessianInLeaf(0.001).setLambdaL1(0.0).setLambdaL2(0.0)
    //      .setBaggingFraction(1.0).setBaggingFreq(0).setBaggingSeed(1).setObjective("regression")
    //      .setLabelCol(labelCol).setCategoricalSlotNames(cateCols).setFeaturesCol("features")
    //      .setBoostingType("rf")

    //    val classifier: LightGBMRegressor = new LightGBMRegressor().setNumIterations(100).setNumLeaves(31)
    //      .setBoostFromAverage(false).setFeatureFraction(1.0).setMaxDepth(-1).setMaxBin(255)
    //      .setLearningRate(0.1).setMinSumHessianInLeaf(0.001).setLambdaL1(0.0).setLambdaL2(0.0)
    //      .setBaggingFraction(0.5).setBaggingFreq(1).setBaggingSeed(1).setObjective("binary")
    //      .setLabelCol(labelCol).setCategoricalSlotNames(cateCols).setFeaturesCol("features")
    //      .setBoostingType("rf")

    //    val classifier: LightGBMRegressor = new LightGBMRegressor().setNumIterations(100).setNumLeaves(31)
    //      .setBoostFromAverage(false).setFeatureFraction(1.0).setMaxDepth(-1).setMaxBin(255)
    //      .setLearningRate(0.1).setMinSumHessianInLeaf(0.001).setLambdaL1(0.0).setLambdaL2(0.0)
    //      .setBaggingFraction(1.0).setBaggingFreq(0).setBaggingSeed(1).setObjective("binary")
    //      .setLabelCol(labelCol).setCategoricalSlotNames(cateCols).setFeaturesCol("features")
    //      .setBoostingType("dart")

    val classifier: LightGBMRegressor = new LightGBMRegressor().setNumIterations(100).setNumLeaves(31)
      .setBoostFromAverage(false).setFeatureFraction(1.0).setMaxDepth(-1).setMaxBin(255)
      .setLearningRate(0.1).setMinSumHessianInLeaf(0.001).setLambdaL1(0.0).setLambdaL2(0.0)
      .setBaggingFraction(1.0).setBaggingFreq(0).setBaggingSeed(1).setObjective("binary")
      .setLabelCol(labelCol).setCategoricalSlotNames(cateCols).setFeaturesCol("features")
      .setBoostingType("goss")

    val pipeline: Pipeline = new Pipeline().setStages(Array(assembler, classifier))

    val Array(tr, te) = originalData.randomSplit(Array(0.7, .03), 666)
    val model = pipeline.fit(tr)
    val modelDF = model.transform(te)
    val evaluator = new BinaryClassificationEvaluator().setLabelCol(labelCol).setRawPredictionCol("prediction")
    println(evaluator.evaluate(modelDF))
    val regressionModel = model.stages(1).asInstanceOf[LightGBMRegressionModel]
    LightGBMUtils.saveToPmml(regressionModel.getModel, "D://Download/regressionModel.xml")
  }


}
