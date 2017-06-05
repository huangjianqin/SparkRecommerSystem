package org.hjqsrs.offline.experiment.collaborativefiltering.userbase

import java.io.PrintWriter

import scala.math._
import scala.io.Source

/**
 * Created by 健勤 on 2016/12/26.
 */
/**
 * 基于用户的协同过滤算法
 */
object Experiment1 {
  def main(args: Array[String]) {
    val datas = Source.fromFile("ml-latest-small/ratings.csv")
    val lines = datas.getLines()

    //去掉第一行
    lines.next()

    //用户评分矩阵
    //Map(userId -> Map(movieId -> rating))
    var ratingMatrix: Map[Int, Map[Int, Double]] = Map()

    for(line <- lines){
      val dataSplits = line.split(",")

      if(!ratingMatrix.contains(dataSplits(0).toInt)){
        ratingMatrix += (dataSplits(0).toInt -> Map())
      }

      ratingMatrix += (dataSplits(0).toInt -> (ratingMatrix(dataSplits(0).toInt) + (dataSplits(1).toInt -> dataSplits(2).toDouble)))
    }

    //每个用户的评分的平均值
    //Map(userId -> avg)
    var avgRatingPerUser: Map[Int, Double] = Map()
    for(u <- ratingMatrix.keys){
      var sum = 0D
      for(r <- ratingMatrix(u).values){
        sum += r
      }
      avgRatingPerUser += (u -> (sum / ratingMatrix(u).size))
    }

    //计算两个用户的相似度,id=1与id=2的用户相似,可以为1::2
    //u开头表示为用户,p开头表示为物品,r开头表示为评分
    //相似度为[-1,+1]
    //Map("userId::userId" -> sim)
    var userSim: Map[String, Double] = Map()
    for(u1 <- ratingMatrix.keys){
      for(u2 <- ratingMatrix.keys){
        //不能计算自己与自己的相似度,和对称性的,比如说id=1与id=2用户的相似度应该等于id=2与id=1用户的相似度
        if(u1 != u2 && !userSim.contains(u2 + "::" + u1)){
          //u1的评分列表
          val u1HasRating = ratingMatrix(u1)
          //u2的评分列表
          val u2HasRating = ratingMatrix(u2)
          //分子
          var sum1 = 0D
          //分母,最后需要开根号的
          var sum2 = 0D
          var sum3 = 0D

          //判断评分物品是否有交集,false为没有
          var isIntersect = false
          for(p <- u1HasRating.keys){
            //只计算u1和u2评分过的物品的交集
            if(u2HasRating.contains(p)){
              isIntersect = true
              val u1p = u1HasRating(p)
              val u2p = u2HasRating(p)

              sum1 += (u1p - avgRatingPerUser(u1)) * (u2p - avgRatingPerUser(u2))
              sum2 += pow(u1p - avgRatingPerUser(u1), 2)
              sum3 += pow(u2p - avgRatingPerUser(u2), 2)

            }
          }

          val sim = sum1 / (sqrt(sum2) * sqrt(sum3))

          val key = u1 + "::" + u2

          if(isIntersect){
            userSim += (key -> sim)
          }
          else{
            userSim += (key -> -1D)
          }

        }
      }
    }

//    println(userSim("1::2"))
//    userSim.foreach((tuple) => println(tuple._1 + "->" + tuple._2))
    saveAsFile(userSim)
  }

  def saveAsFile(userSim: Map[String, Double]): Unit ={
    val pw = new PrintWriter("ml-latest-small/userSim.csv")
    for(keyValue <- userSim){
      pw.println(keyValue._1 + "," + keyValue._2)
    }

    pw.close()
  }
}
