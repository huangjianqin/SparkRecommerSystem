package org.hjqsrs.model.als

import java.io.PrintWriter

import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableOutputFormat, TableInputFormat}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HConstants, HBaseConfiguration}
import org.apache.hadoop.mapreduce.Job
import org.apache.log4j.Logger
import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, ALS, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.hjqsrs.common.{RSConstants, RSContext}
import org.hjqsrs.model.{RSModel, Evaluator}

/**
 * Created by 健勤 on 2017/2/26.
 */
object ALSModel extends Evaluator{
  private val log = Logger.getLogger(ALSModel.getClass)

  def apply(sc: SparkContext, path: String): ALSModel={
    log.info("loading als model >>> " + path)
    val model = MatrixFactorizationModel.load(sc, path)
    log.info("load completed")
    val MSE = evaluate(model, RSContext.context.getRatingsRDD)
    log.info("model MSE>>>>>>>" + MSE)
    return new ALSModel(model, MSE)
  }

  def train(sc: SparkContext, ratingsRDD: RDD[Rating]): ALSModel ={
    //记录取出多少调rating
    log.info("data num: " + ratingsRDD.count())
    val tuple = trainModel(sc, ratingsRDD)
    log.info("model MSE>>>>>>>" + tuple._2)
    return new ALSModel(tuple._1, tuple._2)
  }

  private def trainModel(sc: SparkContext, ratings: RDD[Rating]): Tuple2[MatrixFactorizationModel, Double] ={
    log.info("training als model...")

    val rank = RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_ALS_RANK).toInt
    val numIterations = RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_ALS_NUMITERATIONS).toInt
    val lambda = RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_ALS_LAMBDA).toDouble
    val model = ALS.train(ratings, rank, numIterations, lambda)
    log.info("ALS model train finish")
//    model.save(sc, RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_ALS))
//    log.info("ALS model save completed")
    val MSE = evaluate(model, ratings)


    return (model, MSE)
  }

  def chooseBestModel(ratings: RDD[Rating], param: Array[Tuple3[Int, Int, Int]]): Unit ={
    //存储结果
    import scala.collection.mutable.ListBuffer
    val list = ListBuffer[Tuple4[Int, Int, Double, Double]]()

    log.info("choosing start...")
    val rankParam = param(0)
    val numIterationsParam = param(1)
    val lambdaParam = param(2)
    for(rank <- Range(rankParam._1, rankParam._2, rankParam._3)){
      for(numIterations <- Range(numIterationsParam._1, numIterationsParam._2, numIterationsParam._3)){
        for(tmp <- Range(lambdaParam._1, lambdaParam._2, lambdaParam._3)){
          val lambda = 1.0 * tmp / 1000
          val model = ALS.train(ratings, rank, numIterations, lambda)
          val MSE = evaluate(model, ratings)
          val result = (rank, numIterations, lambda, MSE)
          println(result)
          list += result
        }
      }
    }
    log.info("choose completed")

    //按MSE排序,降序
    val sortedList = list.sortBy(_._4)
    for(tuple <- sortedList){
      println(tuple)
    }
    //以文件形式输出
    val pw = new PrintWriter("bestALSModel.csv")
    for((rank, numIterations, lambda, mse) <- sortedList){
      pw.println(rank + "," + numIterations + "," + lambda + "," + mse)
    }
    pw.close()
  }
}

class ALSModel(private val model: MatrixFactorizationModel, val MSE: Double) extends RSModel with Serializable{
  def getModel = this.model

  override def recommend(usersMovies: RDD[Tuple2[Int, Int]]): RDD[Rating] = {
    return model.predict(usersMovies)
  }

}
