package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper03_GroupBy {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark01_RDD")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(1 to 16)

    //groupBy(func),按照传入函数的返回值进行分组，将相同key对应的值放入一个迭代器
    //分组后的数据形成了元组(k, v)，k表示分组的key，v表示分组的数据集合
    val groupByRDD: RDD[(Int, Iterable[Int])] = listRDD.groupBy(_ % 2)
    groupByRDD.collect().foreach(println)
    sc.stop()
  }
}
