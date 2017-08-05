package org.hjqsrs.offline.clustering

import org.apache.spark.mllib.clustering.KMeans
import org.hjqsrs.common.{RSConstants, RSContext}
import org.apache.spark.mllib.linalg.{Vector, Vectors}

/**
 * Created by 健勤 on 2017/7/31.
 */
object ComputeKinds {
  def computeSimilarity(context: RSContext): Unit ={
    val sc = context.sparkContext
    val kinds = RSContext.getHBaseMovieData(sc).map{movie =>
      movie.getGenresVector()
    }
    kinds.take(10).foreach(println)

    val kMeansModel = KMeans.train(
      kinds,
      RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_KMEANS_KINDS).toInt,
      RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_KMEANS_ITERATION).toInt,
      RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_KMEANS_RUNS).toInt,
      RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_KMEANS_MODE))

//    def error(point: Vector): Double = {
//      val center = kMeansModel.clusterCenters(kMeansModel.predict(point))
//      return Math.sqrt((for(i <- 0 until center.size) yield Math.pow((point(i) - center(i)), 2)).sum)
//    }
//
//    val WSSSE = kinds.map(error(_)).reduce(_ + _)
    val WSSSE = kMeansModel.computeCost(kinds)
    println("Within Set Sum of Squared Error = " + WSSSE)

    //保存KMeans模型
    kMeansModel.save(sc, RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_KMEANS))
  }
}
