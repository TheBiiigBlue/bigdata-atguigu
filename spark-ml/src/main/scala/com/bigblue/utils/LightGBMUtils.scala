package com.bigblue.utils

import java.io.{ByteArrayInputStream, FileOutputStream}

import com.microsoft.ml.spark.lightgbm.LightGBMBooster
import org.jpmml.lightgbm.LightGBMUtil
import org.jpmml.model.MetroJAXBUtil

/**
 * Created By TheBigBlue on 2020/3/20
 * Description : 
 */
object LightGBMUtils {

  def saveToPmml(booster: LightGBMBooster, path: String): Unit = {
    try {
      val gbdt = LightGBMUtil.loadGBDT(new ByteArrayInputStream(booster.model.getBytes))
      import scala.collection.JavaConversions.mapAsJavaMap
      val pmml = gbdt.encodePMML(null, null, Map("compact" -> true))
      MetroJAXBUtil.marshalPMML(pmml, new FileOutputStream(path))
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

}
