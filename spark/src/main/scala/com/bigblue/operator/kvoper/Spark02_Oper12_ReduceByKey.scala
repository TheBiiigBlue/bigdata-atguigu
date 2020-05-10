package com.bigblue.operator.kvoper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper12_ReduceByKey {

  /**
   * 1. reduceByKey：按照key进行聚合，在shuffle之前有combine（预聚合）操作，返回结果是RDD[k,v].
   * 2. groupByKey：按照key进行分组，直接进行shuffle。
   * 3. 开发指导：reduceByKey比groupByKey，建议使用。但是需要注意是否会影响业务逻辑
   */

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark02_Oper12_ReduceByKey")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //reduceByKey(func: (V, V) => V, [numTasks])  在一个(K,V)的RDD上调用，返回一个(K,V)的RDD，
    //使用指定的reduce函数，将相同key的值聚合到一起，reduce任务的个数可以通过第二个可选的参数来设置
    //function的功能是对每两个value之间做什么操作
    val listRDD: RDD[String] = sc.makeRDD(List("AAA", "BBB", "CCC", "DDD", "CCC", "DDD"))
    val pariRDD: RDD[(String, Int)] = listRDD.map((_, 1))
    val reduceByKeyRDD: RDD[(String, Int)] = pariRDD.reduceByKey(_ + _)
    reduceByKeyRDD.collect().foreach(println)
    sc.stop()
  }
}
