package org.hjqsrs.common

import java.util.Properties
import org.apache.hadoop.hbase.client.{Delete, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableOutputFormat, TableInputFormat}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HConstants, HBaseConfiguration}
import org.apache.hadoop.mapreduce.Job
import org.apache.log4j.Logger
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkEnv, SparkConf, SparkContext}

import scala.collection.mutable

/**
 * Created by 健勤 on 2017/2/22.
 */
object RSContext {
  private val log = Logger.getLogger(RSContext.getClass)
  val DEFAULT_CONFIG_PROPERTIES = "config.properties"
  private var rsContext: RSContext = _

  def context = this.rsContext

  def apply(): RSContext ={
    this.rsContext = init(RSContext.DEFAULT_CONFIG_PROPERTIES)
    return this.rsContext
  }

  def apply(configPath: String): RSContext ={
    this.rsContext = init(configPath)
    return this.rsContext
  }

  private def init(configPath: String): RSContext ={
    log.info("init RSContext...")
    //加载配置文件
    val properties = new Properties()
    properties.load(RSContext.getClass.getClassLoader.getResourceAsStream(configPath))
    import collection.JavaConversions._
    for((key, value) <- properties){
      log.info(key + " => " + value)
    }
    val sc = initSC(properties)
    //广播配置文件
    val propertiesBC = sc.broadcast[Properties](properties)
    log.info("RSContext inited")
    return new RSContext(sc, propertiesBC)
  }

  private def initSC(properties: Properties): SparkContext ={
    log.info("init spark context...")
    log.info("spark app name = " + properties.get(RSConstants.SPARK_APPNAME))
    log.info("spark cluster = " + properties.get(RSConstants.SPARK_CLUSTER))
    System.setProperty("spark.driver.memory", "4g")
    System.setProperty("spark.executor.memory", "4g")


    val sparkConf = new SparkConf()
      .setAppName(properties.get(RSConstants.SPARK_APPNAME).toString)
      .setMaster(properties.get(RSConstants.SPARK_CLUSTER).toString)
//      .setJars(Array("E:\\javawebapps\\SparkRecommerSystem\\out\\artifacts\\sparkrecommersystem_jar\\sparkrecommersystem.jar"))

    val sc = new SparkContext(sparkConf)
    log.info("spark context inited")
    return sc
  }


  def getHbaseTrainData(sc: SparkContext): RDD[Rating] ={
    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, context.get(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, context.get(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, context.get(RSConstants.ZOOKEEPER_ZNODE_PARENT))
    hbaseConfig.set(TableInputFormat.INPUT_TABLE, context.get(RSConstants.HJQSRS_HBASE_RATINGS_TABLE))
    hbaseConfig.set(TableInputFormat.SCAN_COLUMN_FAMILY, context.get(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_TRAIN))

    log.info("get hbase rating train data...")
    //以row为单位来区分
    val hbaseData = sc.newAPIHadoopRDD(hbaseConfig, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val colFamilyBytes = Bytes.toBytes(context.get(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_TRAIN))
    val colQualifierBytes = Bytes.toBytes(context.get(RSConstants.HJQSRS_HBASE_RATINGS_QUALIFIER_ALL))
    val ratings = hbaseData.map{tuple =>
      val result = tuple._2

      val rowKey = new String(result.getRow)
      val userId = rowKey.split(":")(0).toInt
      val movieId = rowKey.split(":")(1).toInt
      val rating = new String(result.getValue(colFamilyBytes, colQualifierBytes)).toDouble

      Rating(userId, movieId, rating)
    }
    return ratings
  }

  def getHBaseUsersId(sc: SparkContext): RDD[Int]={
    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, context.get(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, context.get(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, context.get(RSConstants.ZOOKEEPER_ZNODE_PARENT))
    hbaseConfig.set(TableInputFormat.INPUT_TABLE, context.get(RSConstants.HJQSRS_HBASE_USERS_TABLE))
    hbaseConfig.set(TableInputFormat.SCAN_COLUMN_FAMILY, context.get(RSConstants.HJQSRS_HBASE_USERS_COLFAMILY))

    log.info("get hbase user data...")
    //以row为单位来区分
    val hbaseData =sc.newAPIHadoopRDD(hbaseConfig, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val colFamilyBytes = Bytes.toBytes(context.get(RSConstants.HJQSRS_HBASE_USERS_COLFAMILY))
    val colQualifierBytes = Bytes.toBytes(context.get(RSConstants.HJQSRS_HBASE_USERS_INFO_ID))
    val userIds = hbaseData.map{tuple =>
      val result = tuple._2
      val id = new String(result.getValue(colFamilyBytes, colQualifierBytes)).toInt
      id
    }
    log.info("get compeleted")
    return userIds
  }

  def getHBaseMoviesId(sc: SparkContext): RDD[Int]={
    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, context.get(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, context.get(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, context.get(RSConstants.ZOOKEEPER_ZNODE_PARENT))
    hbaseConfig.set(TableInputFormat.INPUT_TABLE, context.get(RSConstants.HJQSRS_HBASE_MOVIES_TABLE))
    hbaseConfig.set(TableInputFormat.SCAN_COLUMN_FAMILY, context.get(RSConstants.HJQSRS_HBASE_MOVIES_COLFAMILY))

    log.info("get hbase movie data...")
    //以row为单位来区分
    val hbaseData = sc.newAPIHadoopRDD(hbaseConfig, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val colFamilyBytes = Bytes.toBytes(context.get(RSConstants.HJQSRS_HBASE_MOVIES_COLFAMILY))
    val colQualifierBytes = Bytes.toBytes(context.get(RSConstants.HJQSRS_HBASE_MOVIES_INFO_ID))
    val movieIds = hbaseData.map{tuple =>
      val result = tuple._2
      val id = new String(result.getValue(colFamilyBytes, colQualifierBytes)).toInt
      id
    }
    log.info("get compeleted")
    return movieIds
  }


}

class RSContext(private val sc: SparkContext, private val propertiesBC: Broadcast[Properties]){
  private val log = RSContext.log

  private var ratings: RDD[Rating] = _
  private var moviesBC: Broadcast[Array[Int]] = _

  def apply(key: String) :String = get(key)

  private def get(key: String) = this.propertiesBC.value.get(key).toString

  def destroy(): Unit ={
    log.info("RSContext destroying ...")
    this.propertiesBC.unpersist()
    this.sc.stop()
    log.info("RSContext destroyed")
  }

  def initData(): Unit ={
    initTrainData()
    initMoiveDataBC()
  }

  def initTrainData(): Unit ={
    //加载数据到Hbase不要调用此方法
    this.ratings = RSContext.getHbaseTrainData(this.sc)
    log.info("评分数: " +  this.ratings.count())
    this.ratings.cache()
  }

  private def initMoiveDataBC(): Unit ={
    val moviesRDD = RSContext.getHBaseMoviesId(this.sc)
    log.info("电影数: " + moviesRDD.count())
    this.moviesBC = sparkContext.broadcast(moviesRDD.toLocalIterator.toArray)
  }

  def sparkContext = this.sc
  def getPropertiesBC = this.propertiesBC
  def getRatingsRDD = this.ratings
  def getMoviesBC = this.moviesBC

}