package org.hjqsrs.online.realtime

import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client.{Result, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HConstants, HBaseConfiguration}
import org.apache.hadoop.mapreduce.Job
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.hjqsrs.common.{RSConstants, RSContext}
import org.hjqsrs.model.als.ALSModel
import org.hjqsrs.online.rs.WeightedHybridRS

/**
 * Created by 健勤 on 2017/2/28.
 */
class KafkaCollector(val groupId: String = RSContext.context(RSConstants.KAFKA_GROUP_ID),
                     val topics: Set[String] = Set(RSContext.context(RSConstants.HJQSRS_TOPIC)),
                      val rs: WeightedHybridRS){
  def run(): Unit ={
    val ssc = new StreamingContext(RSContext.context.sparkContext, Seconds(RSContext.context(RSConstants.SPARK_STREAMING_DURATION).toInt))
    ssc.checkpoint(RSContext.context(RSConstants.SPARK_STREAMING_CHECKPOINT))

    val kafkaConfig = Map(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> RSContext.context(RSConstants.KAFKA_BOOTSTRAP_SERVERS),
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> RSContext.context(RSConstants.KAFKA_KEY_SERIALIZER),
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> RSContext.context(RSConstants.KAFKA_VALUE_SERIALIZER)
    )

    //获取评分
    val ratings = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaConfig, topics).map(_._2).map{rawRating =>
      var rating: Rating = null
      if(rawRating != null && rawRating.matches("\\d*::\\d*::\\d*.\\d*")){
        val splits = rawRating.split("::")
        rating = Rating(splits(0).toInt, splits(1).toInt, splits(2).toDouble)
      }
      rating
    }

    //防止数据丢失
    ratings.cache()
    //不能将DStream转换成RDD
    //那么就只能遍历每个RDD完成操作了
    val rsContext = RSContext.context
    val propertiesBC = rsContext.getPropertiesBC
    val ratingsRDD = rsContext.getRatingsRDD
    val moviesBC = rsContext.getMoviesBC

    //保存评分
//    ratings.foreachRDD{rdd =>
//      val hbaseConfig2 = HBaseConfiguration.create()
//      hbaseConfig2.set(HConstants.ZOOKEEPER_QUORUM, propertiesBC.value.get(RSConstants.HBASE_ZOOKEEPER_QUORUM).toString)
//      hbaseConfig2.set(HConstants.ZOOKEEPER_CLIENT_PORT, propertiesBC.value.get(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT).toString)
//      hbaseConfig2.set(HConstants.ZOOKEEPER_ZNODE_PARENT, propertiesBC.value.get(RSConstants.ZOOKEEPER_ZNODE_PARENT).toString)
//      hbaseConfig2.set(TableOutputFormat.OUTPUT_TABLE, propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_TABLE).toString)
//
//      val job = Job.getInstance(hbaseConfig2)
//      job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
//      job.setOutputKeyClass(classOf[ImmutableBytesWritable])
//      job.setOutputValueClass(classOf[Put])
//
//      rdd.map{case Rating(user, movie, rating) =>
//        val put = new Put(Bytes.toBytes(user + ":" + movie))
//        put.addColumn(Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_TRAIN).toString),
//          Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_QUALIFIER_ALL).toString),
//          Bytes.toBytes(rating.toString))
//        (new ImmutableBytesWritable, put)
//      }.saveAsNewAPIHadoopDataset(job.getConfiguration)
//    }

    //缓存kafka收集到的实时保存的数据,用于在实时推荐时用来过滤用户已评分过的数据
    val onlineHasRating = ratings.map{case Rating(user, movie, rating) =>
      ((user, movie), rating)
    }.updateStateByKey{(values: Seq[Double], now: Option[Double]) =>
      var latest:Double = now.getOrElse(0)
      if(values.size > 0){
        latest = values(0)
      }
      Some(latest)
    }.map{case ((user, movie), rating) =>
      Rating(user, movie, rating)
    }

    onlineHasRating.foreachRDD{rdd =>
      if(!rdd.isEmpty()){
        val sc = rdd.sparkContext

        //合并所有已有评分数据
        val hasRatings = ratingsRDD.union(rdd)
        val hasRatingsUM = hasRatings.map{case Rating(user, movie, rating)=>
          (user, movie)
        }.distinct()

        //模型预测
        //更新缓存在内存中的所有评分
        //为了显示出效果,本质上应该是增量更新,定期重新训练模型,更新模型
        val model = ALSModel.train(sc, hasRatings)
        val weightedHybridRS = WeightedHybridRS(List((model, 1.0)))

        //有新评分的用户ids
        val users = rdd.map{case Rating(user, movie, rating) => user}.distinct()
        val filteredUsersMovies = users.cartesian(sc.parallelize(moviesBC.value)).map((_, 1))
          .leftOuterJoin(hasRatingsUM.map((_, 1))).filter(_._2._2 == None).map(_._1)
        //加权推荐模型预测并保存结果
        weightedHybridRS.recommend(filteredUsersMovies)
      }
    }

    ssc.start()
    ssc.awaitTerminationOrTimeout(RSContext.context(RSConstants.SPARK_STREAMING_TIMEOUTS).toLong * 1000)
    ssc.stop(false)
  }

}

object KafkaCollector{
  def apply(rs: WeightedHybridRS): KafkaCollector ={
    return new KafkaCollector(rs=rs)
  }

  def apply(groupId: String, topics: Set[String], rs: WeightedHybridRS): KafkaCollector ={
    return new KafkaCollector(groupId, topics, rs)
  }
}