package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper02_Glom {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark03_Oper02_Glom")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(1 to 16, 4)
    //将一个分区的数据放到一个数组中，可以使用该算子计算各分区的计算
    //比如做各分区的统计、最大最小值等等
    val glomRDD: RDD[Array[Int]] = listRDD.glom()
    //取各分区最大值
    val maxRDD = glomRDD.map(_.max)

    glomRDD.collect().foreach(arr => println(arr.mkString(",")))
    println
    maxRDD.collect().foreach(println)
    sc.stop()
  }
}
