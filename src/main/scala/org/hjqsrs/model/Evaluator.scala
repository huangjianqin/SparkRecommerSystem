package org.hjqsrs.model

import org.apache.log4j.Logger
import org.apache.spark.mllib.recommendation.{Rating, MatrixFactorizationModel}
import org.apache.spark.rdd.RDD

/**
 * Created by 健勤 on 2017/3/6.
 */
trait Evaluator{
  private val log = Logger.getLogger(classOf[Evaluator])
  def evaluate(model: MatrixFactorizationModel, testData: RDD[Rating]): Double ={
    //log.info("evaluating model quality...")
    val userAndItem = testData.map{case Rating(user, item, rating) => (user, item)}
    val modelPredict = model.predict(userAndItem).map{case Rating(user, item, rating) => ((user, item), rating)}

    val realUserItemRating = testData.map{case Rating(user, item, rating) => ((user, item), rating)}

    val MSE = modelPredict.join(realUserItemRating).map{case ((user, item), (predict, real)) =>
      val err = predict - real
      err * err
    }.mean()
    //log.info("evaluate finish")
    //log.info("MSE = " + MSE)
    return MSE
  }
}
