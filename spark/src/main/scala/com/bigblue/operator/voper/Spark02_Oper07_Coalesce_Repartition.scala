package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper07_Coalesce_Repartition {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark04_Oper07_Coalesce_Repartition")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 1, 5, 2, 9, 6, 1))
    println("分区前分区数： " + listRDD.partitions.size)
    listRDD.glom().collect().foreach(arr => println(arr.mkString(",")))
    //如果coalesce的分区数少于上个rdd的分区数，其实是合并，没有shuffle
    //但是如果指定的分区数很少，并且知道会数据倾斜或者会shuffle，则可以手动指定shuffle为true，使均匀合并
    //如果coalesce的分区数多于上个rdd分区数，则会扩展分区，shuffle
    val coalesceRDD: RDD[Int] = listRDD.coalesce(3)
    println("分区后分区数： " + coalesceRDD.partitions.size)
    coalesceRDD.glom().collect().foreach(arr => println(arr.mkString(",")))

    //或者知道会shuffle或数据倾斜了，则可以使用repartition，均匀合并
    val repartRDD: RDD[Int] = listRDD.repartition(2)
    println("分区后分区数： " + repartRDD.partitions.size)
    repartRDD.glom().collect().foreach(arr => println(arr.mkString(",")))
    sc.stop()
  }
}
