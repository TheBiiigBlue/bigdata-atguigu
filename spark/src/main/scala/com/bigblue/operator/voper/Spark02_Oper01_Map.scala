package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper01_Map {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark02_Oper01_Map")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(1 to 10, 2)
    val mapRDD: RDD[Int] = listRDD.map(_ * 2)

    //这是driver端变量，如果要在executor执行，需要网络传输到executor
    //并且这个变量必须支持序列化
    val no = 2

    //mapPartitions是对分区操作，返回分区集合
    //效率优于map，减少了与执行器的交互次数
    //但是因为是一次性发送一个分区，可能引起Executor的OOM
    //也可以通过该算子获取各个分区的数据量或者不同key的分布量，优化数据倾斜
    val mapParRDD: RDD[Int] = listRDD.mapPartitions(iter => {
      iter.map(_ * no)
    })
    mapParRDD.collect().foreach(println)

    //操作分区，同时可以标注出来分区号，更关注数据在哪个分区
    val mapParWithIndexRDD: RDD[(String, Int)] = listRDD.mapPartitionsWithIndex { case (partNo, iter) => {
      iter.map(("分区号：" + partNo, _))
    }
    }
    mapParWithIndexRDD.collect().foreach(println)

    //flatMap(func),每个输入元素可以被映射为0或多个输出，然后压平
    //所以func应该返回一个序列而不是单一元素
    val arrRDD = sc.makeRDD(Array(Array(1, 2), Array(3, 4)))
    val flatMapRDD = arrRDD.flatMap(x => x)
    flatMapRDD.collect().foreach(println)
    sc.stop()
  }
}
