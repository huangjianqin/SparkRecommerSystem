package org.hjqsrs.offline.experiment.collaborativefiltering.itembase

import java.io.PrintWriter

import scala.io.Source
import math._
/**
 * Created by 健勤 on 2016/12/26.
 */
/**
 * 基于物品的协同过滤算法
 */
object Experiment2 {
  def main(args: Array[String]) {
    val datas = Source.fromFile("ml-latest-small/ratings.csv")
    val lines = datas.getLines()

    //去掉第一行
    lines.next()

    //用户评分矩阵
    //Map(userId -> Map(movieId -> rating))
    var ratingMatrix: Map[Int, Map[Int, Double]] = Map()
    //所有物品Id
    var movieIds: Set[Int] = Set()

    for(line <- lines){
      val dataSplits = line.split(",")

      if(!ratingMatrix.contains(dataSplits(0).toInt)){
        ratingMatrix += (dataSplits(0).toInt -> Map())
      }

      ratingMatrix += (dataSplits(0).toInt -> (ratingMatrix(dataSplits(0).toInt) + (dataSplits(1).toInt -> dataSplits(2).toDouble)))

      //统计movieId->Set
      movieIds += dataSplits(1).toInt
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

    //把数据集切少
    movieIds = movieIds.slice(0, 200)
    //计算两个物品的相似度,id=1与id=2的物品相似,可以为1::2
    //u开头表示为用户,p开头表示为物品,r开头表示为评分
    //相似度为[-1,+1]
    //Map("movieId::movieId" -> sim)
    var movieSim: Map[String, Double] = Map()
    for(p1 <- movieIds){
      for(p2 <- movieIds){
        //不能计算物品自己与物品自己的相似度,和对称性的,比如说id=1与id=2物品的相似度应该等于id=2与id=1物品的相似度
        if(p1 != p2 && !movieSim.contains(p2 + "::" + p1)){
          //分子
          var sum1 = 0D
          //分母,最后需要开根号的
          var sum2 = 0D
          var sum3 = 0D

          //判断评分物品是否有交集,false为没有
          var isIntersect = false
          //遍历用户评分表
          for(u <- ratingMatrix.keys){
            isIntersect = true
            //判断用户是否对两物品都有评分
            val up = ratingMatrix(u)
            if(up.contains(p1) && up.contains(p2)){
              sum1 += (up(p1) - avgRatingPerUser(u)) * (up(p2) - avgRatingPerUser(u))
              sum2 += pow(up(p1) - avgRatingPerUser(u), 2)
              sum3 += pow(up(p2) - avgRatingPerUser(u), 2)
            }
          }

          val sim = sum1 / (sqrt(sum2) * sqrt(sum3))

          val key = p1 + "::" + p2

          if(isIntersect){
            movieSim += (key -> sim)
          }
          else{
            movieSim += (key -> -1D)
          }

        }
      }
    }

    saveAsFile(movieSim)
  }

  def saveAsFile(movieSim: Map[String, Double]): Unit ={
    val pw = new PrintWriter("ml-latest-small/movieSim.csv")
    for(keyValue <- movieSim){
      pw.println(keyValue._1 + "," + keyValue._2)
    }

    pw.close()
  }
}
