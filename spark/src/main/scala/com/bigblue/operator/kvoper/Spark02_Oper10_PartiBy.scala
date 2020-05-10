package com.bigblue.operator.kvoper

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, Partitioner, SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper10_PartiBy {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark04_Oper08_SortBy")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val pairRDD: RDD[(Int, String)] = sc.makeRDD(Array((1, "aaa"), (2, "bbb"), (3, "ccc"), (4, "ddd")))
    println(pairRDD.partitions.size)

    //对pairRDD进行分区操作，如果原有的partitionRDD和现有的partitionRDD是一致的
    //就不进行分区，否则会生成shuffleRDD，产生shuffle
    val partiRDD: RDD[(Int, String)] = pairRDD.partitionBy(new HashPartitioner(2))
    partiRDD.glom().collect().foreach(x => println(x.mkString(",")))
    println

    val myPartiRDD: RDD[(Int, String)] = pairRDD.partitionBy(new MyPartitioner(2))
    myPartiRDD.glom().collect().foreach(x => println(x.mkString(",")))
    sc.stop()
  }
}
//自定义分区器
class MyPartitioner(partitions: Int) extends Partitioner {
  override def numPartitions: Int = {
    partitions
  }

  override def getPartition(key: Any): Int = {
    if(key.toString.toInt < 3) 0 else 1
  }
}
