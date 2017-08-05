package org.hjqsrs.offline.util

import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase._
import org.apache.log4j.Logger
import org.hjqsrs.common.{RSConstants, RSContext}

/**
 * Created by 健勤 on 2017/3/9.
 */
object InitEnviroment {
  private val log = Logger.getLogger(InitEnviroment.getClass)
  private def initData(userNum: Int, movieNum: Int): Unit ={
    log.info("loading data...")
    //加载train至hbase
    SplitData.split(RSContext.context.sparkContext, RSContext.context(RSConstants.HJQSRS_SPARK_DATA_RATINGS), Array(0.6, 0.4), userNum, movieNum)
    //加载用户ID
    LoadInfo.loadUsersInfo(RSContext.context.sparkContext, RSContext.context(RSConstants.HJQSRS_SPARK_DATA_RATINGS), userNum)
    //加载电影信息
    LoadInfo.loadMoviesInfo(RSContext.context.sparkContext, RSContext.context(RSConstants.HJQSRS_SPARK_DATA_MOVIES), movieNum)
    log.info("data loaded")
  }

  private def initTable(): Unit ={
    log.info("initing table...")
    val config = HBaseConfiguration.create()
    config.set(HConstants.ZOOKEEPER_QUORUM, RSContext.context(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    config.set(HConstants.ZOOKEEPER_CLIENT_PORT, RSContext.context(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    config.set(HConstants.ZOOKEEPER_ZNODE_PARENT, RSContext.context(RSConstants.ZOOKEEPER_ZNODE_PARENT))

    val conn = ConnectionFactory.createConnection(config)
    val admin = conn.getAdmin
    val srsTable = TableName.valueOf(RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE))
    val userTable = TableName.valueOf(RSContext.context(RSConstants.HJQSRS_HBASE_USERS_TABLE))
    val movieTable = TableName.valueOf(RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_TABLE))

    //创建srs表
    if(admin.tableExists(srsTable)){
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE) + "' table existed")
      admin.disableTable(srsTable)
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE) + "' table disabled")
      admin.deleteTable(srsTable)
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE) + "' table deleted")
    }
    log.info("ready to create new '" + RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE) + "' table...")
    val srsDescriptor = new HTableDescriptor(srsTable)
    srsDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_RESULT))))
    srsDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_TEST))))
    srsDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_TRAIN))))
    admin.createTable(srsDescriptor)
    log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE) + "' table created")

    //创建user表
    if(admin.tableExists(userTable)){
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_USERS_TABLE) + "' table existed")
      admin.disableTable(userTable)
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_USERS_TABLE) + "' table disabled")
      admin.deleteTable(userTable)
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_USERS_TABLE) + "' table deleted")
    }
    log.info("ready to create new '" + RSContext.context(RSConstants.HJQSRS_HBASE_USERS_TABLE) + "' table...")
    val userDescriptor = new HTableDescriptor(userTable)
    userDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(RSContext.context(RSConstants.HJQSRS_HBASE_USERS_COLFAMILY))))
    admin.createTable(userDescriptor)
    log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_USERS_TABLE) + "' table created")

    //创建movie表
    if(admin.tableExists(movieTable)){
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_TABLE) + "' table existed")
      admin.disableTable(movieTable)
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_TABLE) + "' table disabled")
      admin.deleteTable(movieTable)
      log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_TABLE) + "' table deleted")
    }
    log.info("ready to create new '" + RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_TABLE) + "' table")
    val movieDescriptor = new HTableDescriptor(movieTable)
    movieDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_COLFAMILY))))
    admin.createTable(movieDescriptor)
    log.info("'" + RSContext.context(RSConstants.HJQSRS_HBASE_MOVIES_TABLE) + "' table created")

    log.info("all table inited")
  }

  def initEnviroment(): Unit ={
    //初始化Context
    RSContext()
    initTable()
    initData(100, 2000)
  }
}
