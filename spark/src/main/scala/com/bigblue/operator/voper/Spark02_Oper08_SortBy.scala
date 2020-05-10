package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper08_SortBy {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark04_Oper08_SortBy")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 1, 5, 2, 9, 6, 1))

    //sortBy(f: (T) => K, 升降序 = true, numPartitions)
    //sortBy是一个shuffle操作
    val sortRDD: RDD[Int] = listRDD.sortBy(x => x, true)
    sortRDD.collect().foreach(println)
    sc.stop()
  }
}
