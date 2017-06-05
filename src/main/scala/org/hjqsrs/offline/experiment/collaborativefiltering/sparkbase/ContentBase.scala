package org.hjqsrs.offline.experiment.collaborativefiltering.sparkbase

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by 健勤 on 2017/1/11.
 */
object ContentBase {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("ContentBase").setMaster("local[5]")
    val sc = new SparkContext(conf)

    val rawData = sc.textFile("ml-latest-small/movies.csv").filter(_.matches("\\d*,.*,.*")).map{data =>
      val splits = data.split(",")
      val movieId = splits(0)
      val features = splits(splits.length - 1).split("\\|").toSeq
      (movieId, features)
    }

    //转换成((movie1_id, movie2_id), (movie1_feature, movie2_feature))
    //full join ,要过滤掉重复的,所以movie1_id < movie2_id
    //以便计算所有电影间的相似度
    val formattedRawData = rawData.map((1, _))
    val featuresMatrix = formattedRawData.join(formattedRawData).map{data =>
      val movie1 = data._2._1
      val movie2 = data._2._2
      ((movie1._1.toInt, movie2._1.toInt), (movie1._2, movie2._2))
    }.filter{data =>
      val movie1Id = data._1._1
      val movie2Id = data._1._2
      movie1Id != movie2Id
    }

    //最后格式是(movie1, (movie2, sim))
    //过滤掉相似度<0.8
    import math._
    val simMatrix = featuresMatrix.map{twoFeature =>
      val feature1 = twoFeature._2._1
      val feature2 = twoFeature._2._2

      val sim = feature1.intersect(feature2).length.toDouble / sqrt(feature1.length * feature2.length)

      (twoFeature._1, sim)
    }.filter(_._2 > 0.8).map{tuple2 =>
      (tuple2._1._1, (tuple2._1._2, tuple2._2))
    }

    //获取用户评分矩阵
    //(userId, movieId, rating)
    val tmp = sc.textFile("ml-latest-small/ratings.csv").randomSplit(Array(0.8, 0.2))
    val ratings = tmp(0).filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*")).map{line =>
            val splits = line.split(",")
            (splits(0).toInt, splits(1).toInt, splits(2).toDouble)
          }
//    val ratings = sc.textFile("ml-data/data.csv").filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*")).map{line =>
//      val splits = line.split(",")
//      (splits(0).toInt, splits(1).toInt, splits(2).toDouble)
//    }

    //获取用户要预测评分的所有电影
    //join后(用户评分过的movieId, ((userId, rating), (sim_movieId, sim)))
    //结果格式:((userId, sim_movieId), predictRating)
    val predictResults = ratings.map{tuple3 =>
      (tuple3._2, (tuple3._1, tuple3._3))
    }.join(simMatrix).map{tuple3 =>
      val rating = tuple3._2._1._2
      val sim = tuple3._2._2._2
      val predictRating = sim * (rating - 2.5)
      ((tuple3._2._1._1, tuple3._2._2._1), predictRating)
    }.reduceByKey(_ + _)

//    val recommendResult = predictResults.map{predictResult =>
//      (predictResult._2, predictResult._1)
//    }.sortByKey(ascending = false).take(500).foreach(println)

    val testData = tmp(1).filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*")).map{line =>
      val splits = line.split(",")
      ((splits(0).toInt, splits(1).toInt), splits(2).toDouble)
    }
//    val testData = sc.textFile("ml-data/test.csv").filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*")).map{line =>
//      val splits = line.split(",")
//      ((splits(0).toInt, splits(1).toInt), splits(2).toDouble)
//    }

    val MSE = testData.join(predictResults).map { case ((user, product), (r1, r2)) =>
      val err = (r1 - r2)
      err * err
    }.mean()

    sc.stop()

    println(MSE)

  }
}
