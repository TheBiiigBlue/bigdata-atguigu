package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper06_Distinct {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark01_RDD")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 1, 5, 2, 9, 6, 1))

    //对rdd数据去重，有shuffle，且会乱序。底层使用了reduceByKey
    //因为去重后数据量少了，可以缩小分区
    val distinctRDD: RDD[Int] = listRDD.distinct(2)
    distinctRDD.collect().foreach(println)
    sc.stop()
  }
}
