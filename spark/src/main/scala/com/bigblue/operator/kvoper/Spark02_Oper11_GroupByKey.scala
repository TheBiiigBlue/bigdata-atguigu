package com.bigblue.operator.kvoper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper11_GroupByKey {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark02_Oper11_GroupByKey")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //groupByKey也是对每个key进行操作，对value生成一个迭代器
    val listRDD: RDD[String] = sc.makeRDD(List("AAA", "BBB", "CCC", "DDD", "CCC", "DDD"))
    val pariRDD: RDD[(String, Int)] = listRDD.map((_, 1))
    val groupByKeyRDD: RDD[(String, Iterable[Int])] = pariRDD.groupByKey()
    val strRDD = groupByKeyRDD.map(x => (x._1, x._2.toArray.mkString(",")))
    strRDD.collect().foreach(println)
    sc.stop()
  }
}
