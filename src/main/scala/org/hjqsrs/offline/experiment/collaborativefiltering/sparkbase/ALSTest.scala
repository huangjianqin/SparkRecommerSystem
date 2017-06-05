package org.hjqsrs.offline.experiment.collaborativefiltering.sparkbase

import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, ALS, Rating}
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by 健勤 on 2017/1/4.
 */
object ALSTest {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("CollaborativeFilteringExample").setMaster("local[5]")
    val sc = new SparkContext(conf)

    // $example on$
    // Load and parse the data
    val data = sc.textFile("ml-data/data.csv")

    val ratings = data.filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*")).map(_.split(',') match { case Array(user, item, rate, timestamp) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    // Build the recommendation model using ALS
    val rank = 150
    val numIterations = 10
    val model = ALS.train(ratings, rank, numIterations, 0.01)

    // Evaluate the model on rating data
    val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }
    val predictions =
      model.predict(usersProducts).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
    val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }.join(predictions)
    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
      val err = (r1 - r2)
      err * err
    }.mean()
    sc.stop()

    println("Mean Squared Error = " + MSE)

    // Save and load model
//    model.save(sc, "myCollaborativeFilter")
//    val sameModel = MatrixFactorizationModel.load(sc, "myCollaborativeFilter")
    // $example off$

  }
}
