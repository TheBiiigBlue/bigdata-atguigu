package com.bigblue.operator.kvoper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper13_AggregateByKey {

  /**
   * 参数：(zeroValue:U,[partitioner: Partitioner]) (seqOp: (U, V) => U,combOp: (U, U) => U)
   * 1. 作用：在kv对的RDD中，，按key将value进行分组合并，合并时，将每个value和初始值作为seq函数的参数，进行计算，返回的结果作为一个新的kv对，然后再将结果按照key进行合并，最后将每个分组的value传递给combine函数进行计算（先将前两个value进行计算，将返回结果和下一个value传给combine函数，以此类推），将key与计算结果作为一个新的kv对输出。
   * 2. 参数描述：
   * （1）zeroValue：给每一个分区中的每一个key一个初始值；
   * （2）seqOp：函数用于在每一个分区中用初始值逐步迭代value；
   * （3）combOp：函数用于合并每个分区中的结果。
   *
   */

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark02_Oper13_AggregateByKey")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //reduceByKey(func: (V, V) => V, [numTasks])  在一个(K,V)的RDD上调用，返回一个(K,V)的RDD，
    //使用指定的reduce函数，将相同key的值聚合到一起，reduce任务的个数可以通过第二个可选的参数来设置
    //function的功能是对每两个value之间做什么操作
    val listRDD: RDD[String] = sc.makeRDD(List("AAA", "BBB", "CCC", "DDD", "CCC", "DDD"))
    val pariRDD: RDD[(String, Int)] = listRDD.map((_, 1))
    pariRDD.aggregateByKey()
//    reduceByKeyRDD.collect().foreach(println)
    sc.stop()
  }
}
