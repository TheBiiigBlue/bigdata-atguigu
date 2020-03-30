package com.bigblue.smote

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.ml.linalg.BLAS.axpy
import org.apache.spark.ml.linalg._
import org.apache.spark.rdd.RDD

import scala.util.Random

/**
 * Created By TheBigBlue on 2020/3/25
 * Description : 
 */
object SmoteSampler {

  def generateSamples(data: RDD[(Long, Vector)], k: Int, N: Int): RDD[Vector] = {
    //笛卡尔积
    val kneiPre = data.cartesian(data).map { case ((id1, vec1), (id2, vec2)) =>
      (id1, vec1, vec2)
    }
    print(kneiPre.count() + "\t" + new SimpleDateFormat("HH:mm:ss").format(new Date()))
    val knei: RDD[(Vector, Array[Vector])] = kneiPre.groupBy(_._1).map { case (id, iter) =>
      val arr = iter.toArray
      val tailArr: Array[(Long, Vector, Vector)] = arr.sortBy(t => Vectors.sqdist(t._2, t._3)).take(k + 1).tail
      (arr(0)._2, tailArr.map(_._3))
    }
    print(knei.count() + "\t" + new SimpleDateFormat("HH:mm:ss").format(new Date()))
    knei.foreach(t => println(t._1 + "\t" + t._2.mkString(", ")))

    knei.flatMap { case (vec, neighbours) =>
      (1 to N).map { i =>
        val rn = neighbours(Random.nextInt(k))
        val diff = rn.copy
        axpy(-1.0, vec, diff)
        val newVec = vec.copy
        axpy(Random.nextDouble(), diff, newVec)
        newVec
      }.iterator
    }
  }

  def generateSamples1(data: RDD[(Long, Vector)], k: Int, N: Int): RDD[Vector] = {
    val groupedRDD = data.groupBy(_._1)
    val vecAndNeis: RDD[(Vector, Array[Vector])] = groupedRDD.flatMap { case (id, iter) =>
      val vecArr = iter.toArray.map(_._2)
      //对每个vector产生笛卡尔积
      val cartesianArr: Array[(Vector, Vector)] = vecArr.flatMap(vec1 => {
        vecArr.map(vec2 => (vec1, vec2))
      }).filter(tuple => tuple._1 != tuple._2)
      cartesianArr.groupBy(_._1).map { case (vec, vecArr) => {
        (vec, vecArr.sortBy(x => Vectors.sqdist(x._1, x._2)).take(k).map(_._2))
      }
      }
    }

    //1.从这k个近邻中随机挑选一个样本，以该随机样本为基准生成N个新样本
    val vecRDD = vecAndNeis.flatMap { case (vec, neighbours) =>
      (1 to N).map { i =>
        val rn = neighbours(Random.nextInt(k))
        val diff = rn.copy
        axpy(-1.0, vec, diff)
        val newVec = vec.copy
        axpy(Random.nextDouble(), diff, newVec)
        newVec
      }.iterator
    }
    vecRDD
  }

}
