package org.hjqsrs.offline.clustering

import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.recommendation.Rating
import org.hjqsrs.common.{RSContext, Movie}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
 * Created by 健勤 on 2017/7/31.
 */
class CoolRecommend(val model: KMeansModel){
  def recommend(movies: Array[Movie]): Array[Movie] = {
    val sc = RSContext.context.sparkContext
    val clustersBC = sc.broadcast(movies.map{movie =>
      model.predict(movie.getGenresVector())
    }.distinct)

    val movieId2Movie = RSContext.getHBaseMovieData(sc).map{movie =>
      (movie.id, movie)
    }
    val top20 = RSContext.getHbaseTrainData(sc).map{case Rating(userId, movieId, rating) =>
      (movieId, (userId, rating))
    }.join(movieId2Movie).map{case (movieId, ((userId, rating), movie)) =>
      (userId, ListBuffer(Movie(movie.id, movie.name, movie.genres, rating)))
    }.reduceByKey(_ ++ _).map{case (userId, moviess) =>
      val cluster2Rating = moviess.map{movie =>
        if(clustersBC.value.contains(model.predict(movie.getGenresVector())))
          movie.rating
        else
          0
      }

      val sim = cluster2Rating.reduce(_ + _) * 1.0 / movies.size

      (userId, (moviess, sim))
    }.sortBy(_._2._2, false).take(20)

    val result = ArrayBuffer[Movie]()
    for((userId, (moviess, sim)) <- top20){
      //选出评分较高(3分以上),且用户没有选中的电影
      val possible = moviess.filter(_.rating >= 3).diff(movies)
      result ++= possible
    }

    result.distinct.toArray
  }
}
