package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper04_Filter {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark01_RDD")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(1 to 16)

    //根据指定的规则过滤
    val filterRDD: RDD[Int] = listRDD.filter(_ % 2 == 0)
    filterRDD.collect().foreach(println)
    sc.stop()
  }
}
