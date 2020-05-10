package com.bigblue.operator.voper

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created By TheBigBlue on 2020/4/19
 * Description : 
 */
object Spark02_Oper05_Sample {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark01_RDD")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val listRDD: RDD[Int] = sc.makeRDD(1 to 16)

    //sample(withReplacement, fraction, seed)
    //以指定的随机种子随机抽样出大于fraction分数的样本，withReplacement表示
    //抽出的数据是否放回，true为有放回的抽样，false为无放回抽样
    //fraction为分数，seed为随机种子，
    //可以通过抽样来排查数据倾斜
    val sampleRDD = listRDD.sample(false, 0.8, 1)
    sampleRDD.collect().foreach(println)
    sc.stop()
  }
}
