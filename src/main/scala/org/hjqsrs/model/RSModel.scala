package org.hjqsrs.model

import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD

/**
 * Created by 健勤 on 2017/3/6.
 */
trait RSModel {
  def recommend(usersMovies: RDD[Tuple2[Int, Int]]): RDD[Rating]
}
