package org.hjqsrs.common

import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.hjqsrs.offline.util.Utils

/**
 * Created by 健勤 on 2017/7/31.
 */
case class Movie(id: Int, name: String, genres: String, rating: Double) {
  def getGenresVector(): Vector ={
    Utils.kinds2Vector(genres)
  }
}
