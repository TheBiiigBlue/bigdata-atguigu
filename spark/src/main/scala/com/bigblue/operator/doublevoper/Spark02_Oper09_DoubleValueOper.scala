package com.bigblue.operator.doublevoper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper09_DoubleValueOper {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark02_Oper09_DoubleValueOper")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //union  operate，计算两个rdd的并集
    val listRDD: RDD[Int] = sc.makeRDD(1 to 5)
    val listRDD2 = sc.makeRDD(6 to 10)
    val unionRDD: RDD[Int] = listRDD.union(listRDD2)
    unionRDD.collect().foreach(println)
    println

    //subtract operate，计算第一个rdd中不在第二个rdd的值
    val listRDD3: RDD[Int] = sc.makeRDD(3 to 8)
    val listRDD4: RDD[Int] = sc.makeRDD(0 to 5)
    val subtractRDD: RDD[Int] = listRDD3.subtract(listRDD4)
    subtractRDD.collect().foreach(println)
    println

    //intersection operate，计算两个rdd的交集
    val intersectionRDD: RDD[Int] = listRDD3.intersection(listRDD4)
    intersectionRDD.collect().foreach(println)
    println

    //cartesian operate，计算两个rdd的笛卡尔积，避免使用
    val cartesianRDD: RDD[(Int, Int)] = listRDD3.cartesian(listRDD4)
    cartesianRDD.collect().foreach(println)
    println

    //zip operate，拉链操作，合成tuple
    //两个rdd数量必须一致，两个分区必须一样多
    val listRDD5: RDD[Int] = sc.makeRDD(3 to 8/**, 3*/)
    val listRDD6: RDD[Int] = sc.makeRDD(0 to 5/**, 2*/)
    val zipRDD: RDD[(Int, Int)] = listRDD5.zip(listRDD6)
    zipRDD.collect().foreach(println)
    println
    sc.stop()
  }
}
