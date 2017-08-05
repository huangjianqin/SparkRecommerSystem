package org.hjqsrs.offline.util

import org.apache.spark.mllib.linalg.{Vector, Vectors}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by 健勤 on 2017/7/31.
 */
object Utils {
  val kindMap = Map("Action" -> 0, "Adventure" -> 1, "Animation" -> 2, "Children" -> 3, "Comedy" -> 4, "Crime" -> 5, "Documentary" -> 6,
    "Drama" -> 7, "Fantasy" -> 8, "Film-Noir" -> 9, "Horror" -> 10, "Musical" -> 11, "Mystery" -> 12, "Romance" -> 13, "Sci-Fi" -> 14,
    "Thriller" -> 15, "War" -> 16, "Western" -> 17, "(no genres listed)" -> 18, "IMAX" -> 19)
  val kindNum = 20
  def kinds2Vector(kinds: String): Vector={
    val array = ArrayBuffer[Double](0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    for(kind <- kinds.split("\\|")){
      array(kindMap(kind)) = 1.0
    }
    Vectors.dense(array.toArray)
  }

}
