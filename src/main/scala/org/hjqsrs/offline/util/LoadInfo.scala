package org.hjqsrs.offline.util

import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.{HConstants, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.hjqsrs.common.{RSConstants, RSContext}

/**
 * Created by 健勤 on 2017/3/6.
 */
object LoadInfo {
  private val log = Logger.getLogger(LoadInfo.getClass)
  def loadUsersInfo(sc: SparkContext, file: String, userNum: Int): Unit ={
    log.info("loading users info to hbase...")
    val rawData = sc.textFile(file).filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*"))
    val propertiesBC = RSContext.context.getPropertiesBC
    val userIds = rawData.map{line =>
      val userId = line.split(",")(0)
      userId
    }.distinct().filter{userId =>
      userId.toInt <= userNum
    }.map{case userId =>

      val put = new Put(Bytes.toBytes(userId))
      put.addColumn(Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_USERS_COLFAMILY).toString),
        Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_USERS_INFO_ID).toString), Bytes.toBytes(userId))

      (new ImmutableBytesWritable, put)
    }
    log.info("user id num =>" + userIds.count())

    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, RSContext.context(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, RSContext.context(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, RSContext.context(RSConstants.ZOOKEEPER_ZNODE_PARENT))
    hbaseConfig.set(TableOutputFormat.OUTPUT_TABLE, RSContext.context(RSConstants.HJQSRS_HBASE_USERS_TABLE))

    val job = Job.getInstance(hbaseConfig)
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])

    userIds.saveAsNewAPIHadoopDataset(job.getConfiguration)
    log.info("users info loaded")
  }

  def loadMoviesInfo(sc: SparkContext, file: String, movieNum: Int): Unit ={
    log.info("loading movies info to hbase...")
    val rawData = sc.textFile(file).filter(_.matches("\\d*,.*"))
    log.info("all movie info num =>" + rawData.count())
    val propertiesBC = RSContext.context.getPropertiesBC
    val moviesInfo = rawData.filter{line =>
      val movie = line.split(",")(0).toInt
      movie <= movieNum
    }.map{line =>
      var movieId:String = ""
      var movieName:String = ""
      var movieGenres:String = ""

      if(line.contains("\"")){
        movieId = line.split(",\"")(0)
        movieName = line.split(",\"")(1).split("\",")(0)
        movieGenres = line.split(",\"")(1).split("\",")(1)
      }
      else{
        movieId = line.split(",")(0)
        movieName = line.split(",")(1)
        movieGenres = line.split(",")(2)
      }


      val put = new Put(Bytes.toBytes(movieId))
      put.addColumn(Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_MOVIES_COLFAMILY).toString), Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_MOVIES_INFO_ID).toString), Bytes.toBytes(movieId))
      put.addColumn(Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_MOVIES_COLFAMILY).toString), Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_MOVIES_INFO_NAME).toString), Bytes.toBytes(movieName))
      put.addColumn(Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_MOVIES_COLFAMILY).toString), Bytes.toBytes(propertiesBC.value.get(RSConstants.HJQSRS_HBASE_MOVIES_INFO_GENRES).toString), Bytes.toBytes(movieGenres))

      (new ImmutableBytesWritable, put)
    }
    log.info("movie info num =>" + moviesInfo.count())


    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, RSContext.context(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, RSContext.context(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, RSContext.context(RSConstants.ZOOKEEPER_ZNODE_PARENT))
    hbaseConfig.set(TableOutputFormat.OUTPUT_TABLE, RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_TABLE))

    val job = Job.getInstance(hbaseConfig)
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])

    moviesInfo.saveAsNewAPIHadoopDataset(job.getConfiguration)
    log.info("movies info loaded")
  }
}
