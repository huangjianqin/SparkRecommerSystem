package org.hjqsrs.offline.util

import java.io.PrintWriter

import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HConstants, HBaseConfiguration, TableName}
import org.apache.hadoop.mapreduce.Job
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.hjqsrs.common.{RSConstants, RSContext}

/**
 * Created by 健勤 on 2017/2/22.
 */
object SplitData {
  val log = Logger.getLogger(SplitData.getClass)

  /**
   * 使用spark hbase的结合方式,
   * @param sc
   * @param ratio
   */
  def split(sc: SparkContext, file: String, ratio: Array[Double], userNum: Int, movieNum: Int): Unit ={
    log.info("spliting rating data...")
    val allData = sc.textFile(file)
    val splitedData = allData.filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*")).filter{line =>
      val user = line.split(",")(0).toInt
      val movie = line.split(",")(1).toInt
      user <= userNum && movie <= movieNum
    }.randomSplit(ratio)
    val trainData = splitedData(0)
    val testData = splitedData(1)
    log.info("train data num =>" + trainData.count())
    log.info("test data num =>" + testData.count())

    log.info("output train data to hbase...")
    val hbaseConfig = HBaseConfiguration.create()
    hbaseConfig.set(TableOutputFormat.OUTPUT_TABLE, RSContext.context(RSConstants.HJQSRS_HBASE_RATINGS_TABLE))
    hbaseConfig.set(HConstants.ZOOKEEPER_QUORUM, RSContext.context(RSConstants.HBASE_ZOOKEEPER_QUORUM))
    hbaseConfig.set(HConstants.ZOOKEEPER_CLIENT_PORT, RSContext.context(RSConstants.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT))
    hbaseConfig.set(HConstants.ZOOKEEPER_ZNODE_PARENT, RSContext.context(RSConstants.ZOOKEEPER_ZNODE_PARENT))

    val job = Job.getInstance(hbaseConfig)
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputKeyClass(classOf[Put])

    val propertiesBC = RSContext.context.getPropertiesBC
    trainData.map(
      line2Put(_, propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_TRAIN).toString,
        propertiesBC.value.get(RSConstants.HJQSRS_HBASE_RATINGS_QUALIFIER_ALL).toString))
      .saveAsNewAPIHadoopDataset(job.getConfiguration)

    log.info("output test data to hbase...")
    //testData.map(line2Put(_, RSContext(RSConstants.HJQSRS_HBASE_RATINGS_COLFAMILY_TEST))).saveAsNewAPIHadoopDataset(job.getConfiguration)
    //不存放在hbase中,因为hbase会排序,取出一定数量的评分时就会存在都是那么几个用户了,那么对于更新模型来说比较差
    //web UI使用该文件去随机发送消息
    val pw = new PrintWriter("test/testRatings.csv")
    testData.toLocalIterator.foreach(pw.println(_))
    pw.close()
    log.info("save train and test rating data completed")
  }

  private def line2Put(line: String, colFamily: String, colQualifier: String): Tuple2[ImmutableBytesWritable, Put]={
    val userId = line.split(",")(0)
    val movieId = line.split(",")(1)
    val rating = line.split(",")(2)

    val put = new Put(Bytes.toBytes(userId + ":" + movieId));
    put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(colQualifier), Bytes.toBytes(rating))

    return (new ImmutableBytesWritable, put)
  }

}
