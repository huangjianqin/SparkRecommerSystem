package org.hjqsrs.online.rs

import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HConstants, HBaseConfiguration}
import org.apache.hadoop.mapreduce.Job
import org.apache.log4j.Logger
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD
import org.hjqsrs.common.{RSConstants, RSContext}
import org.hjqsrs.model.RSModel
import org.hjqsrs.model.als.ALSModel

/**
 * Created by 健勤 on 2017/3/6.
 */
class WeightedHybridRS(private val rsmodels: List[Tuple2[RSModel, Double]]) extends RSModel with Serializable{
  def getAllRS = rsmodels

  override def recommend(usersMovies: RDD[Tuple2[Int, Int]]): RDD[Rating] = {
    var result: RDD[Rating] = null
    //分别计算每个模型的预测评分
    for((model, ratio) <- rsmodels){
      if(result == null){
        result = model.recommend(usersMovies).map{case Rating(user, movie, rating) =>
          Rating(user, movie, rating * ratio)
        }
      }
      else{
        val tmp = model.recommend(usersMovies).map{case Rating(user, movie, rating) =>
          Rating(user, movie, rating * ratio)
        }

        result = result.union(tmp)
      }
    }

    //加权混合
    val hybridResult = result.map{case Rating(user, movie, rating) =>
      ((user, movie), rating)
    }.reduceByKey(_ + _).map{case ((user, movie), rating) =>
      Rating(user, movie, rating)
    }

    //取出前20,并格式化结果
    val formattedResultRDD = hybridResult.map{case Rating(user, movie, rating) =>
      (user, List((movie, rating)))
    }.reduceByKey(_ ::: _).map{case (user, list)=>
      val top20 = list.sortBy(_._2).takeRight(20)
      var formattedResult = top20(0)._1 + "&" + top20(0)._2
      for(i <- Range(1, 19)){
        formattedResult += "::" + top20(i)._1 + "&" + top20(i)._2
      }
      (user, formattedResult)
    }

//    println(formattedResultRDD.take(20).mkString("\r\n"))
    //保存推荐结果到HBase
    saveResult2(formattedResultRDD)

    return hybridResult
  }

  /**
   * 取top20,并固定rowkey,下次预测时就可以覆盖,cell value是一个top20的格式化结果
   * movie1&rating1::movie2&rating2.....
   * @param result
   */
  private def saveResult2(result: RDD[Tuple2[Int, String]]): Unit ={
    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, RSContext.context(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, RSContext.context(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, RSContext.context(RSConstants.ZOOKEEPER_ZNODE_PARENT))
    hbaseConfig.set(TableOutputFormat.OUTPUT_TABLE, RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE))

    val job = Job.getInstance(hbaseConfig)
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])

    val propertiesBC = RSContext.context.getPropertiesBC
    result.map{case (user, resultStr) =>
      val put = new Put(Bytes.toBytes(user + ":predict"))
      WeightedHybridRS.log.info("predict result change(userid) =>" + user)
      put.addColumn(Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_RESULT).toString),
        Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_QUALIFIER_ALL).toString),
        Bytes.toBytes(resultStr))
      (new ImmutableBytesWritable, put)
    }.saveAsNewAPIHadoopDataset(job.getConfiguration)
  }

  private def saveResult(result: RDD[Tuple2[Tuple2[Int, Int], Double]]): Unit ={
    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, RSContext.context(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, RSContext.context(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, RSContext.context(RSConstants.ZOOKEEPER_ZNODE_PARENT))
    hbaseConfig.set(TableOutputFormat.OUTPUT_TABLE, RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE))

    val job = Job.getInstance(hbaseConfig)
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])

    val propertiesBC = RSContext.context.getPropertiesBC
    result.map{case ((user, movie), rating) =>
      val put = new Put(Bytes.toBytes(user + ":" + movie))
      put.addColumn(Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_RESULT).toString),
        Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_QUALIFIER_ALL).toString),
        Bytes.toBytes(rating.toString))
      (new ImmutableBytesWritable, put)
    }.saveAsNewAPIHadoopDataset(job.getConfiguration)
  }

}

object WeightedHybridRS{
  private val log = Logger.getLogger(WeightedHybridRS.getClass)
  def apply(rsmodels: List[Tuple2[RSModel, Double]]) = new WeightedHybridRS(rsmodels)
  def apply(rs: WeightedHybridRS, rsmodels: List[Tuple2[RSModel, Double]]) = new WeightedHybridRS(rs.getAllRS ++ rsmodels)

  /**
   * 对于每次启动前,都需要先对所有用户对应没有评分过的电影预测,这部分用户量大,所以只能离线计算好
   * 然后再根据kafka流来对ALS模型进行调整
   */
  def initialPredict(weightedHybridRS: WeightedHybridRS): Unit ={
    val rsContext = RSContext.context
    //所有用户id
    val users = RSContext.getHBaseUsersId(rsContext.sparkContext)
    //所有电影Id
    val movies = RSContext.getHBaseMoviesId(rsContext.sparkContext)
    val filteredUsersMovies = users.cartesian(movies).map((_, 1)).leftOuterJoin(rsContext.getRatingsRDD.map{case Rating(user, movie, rating) =>
      ((user, movie), 1)
    }).filter(_._2._2 == None).map(_._1)

    //加权推荐模型预测并保存结果
    weightedHybridRS.recommend(filteredUsersMovies)
  }
}