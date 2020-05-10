package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark01_RDD {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark01_RDD")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //1. 从集合中创建
    //1.1 从内存中创建  makeRDD，底层就是调用parallize
    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    //1.2 从内存中创建  parallelize
    val arrRDD: RDD[Int] = sc.parallelize(Array(1, 2, 3, 4))
    listRDD.collect().foreach(println)

    //2. 从外部存储中创建，传递的分区参数为最小分区数，但不一定是这个分区数
    // 取决于hadoop读取文件时分片规则
    val fileRDD: RDD[String] = sc.textFile("data/hour.csv", 2)
    fileRDD.saveAsTextFile("spark/output/01")
    sc.stop()
  }

}
