package org.hjqsrs.common

/**
 * Created by 健勤 on 2017/3/6.
 */
object RSConstants {
  //spark配置
  val SPARK_CLUSTER = "spark.cluster"
  val SPARK_APPNAME = "spark.appname"
  val SPARK_STREAMING_CHECKPOINT = "spark.streaming.checkpoint"
  val SPARK_STREAMING_DURATION = "spark.streaming.duration"
  val SPARK_STREAMING_TIMEOUTS = "spark.streaming.timeoutS"

  //hbase配置
  val HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum"
  val HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT = "hbase.zookeeper.property.clientPort"
  val ZOOKEEPER_ZNODE_PARENT = "zookeeper.znode.parent"

  //kafka配置
  val KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers"
  val KAFKA_KEY_SERIALIZER = "kafka.key.serializer"
  val KAFKA_VALUE_SERIALIZER = "kafka.value.serializer"
  val KAFKA_GROUP_ID = "kafka.group.id"

  //hjqsrs系统配置
  val HJQSRS_SPARK_DATA_RATINGS = "hjqsrs.spark.data.ratings"
  val HJQSRS_SPARK_DATA_MOVIES = "hjqsrs.spark.data.movies"
  val HJQSRS_SPARK_MODEL_ALS = "hjqsrs.spark.model.als"
  val HJQSRS_SPARK_MODEL_ALS_RANK = "hjqsrs.spark.model.als.rank"
  val HJQSRS_SPARK_MODEL_ALS_NUMITERATIONS = "hjqsrs.spark.model.als.numIterations"
  val HJQSRS_SPARK_MODEL_ALS_LAMBDA = "hjqsrs.spark.model.als.lambda"

  val HJQSRS_SPARK_MODEL_KMEANS = "hjqsrs.spark.model.kmeans"
  val HJQSRS_SPARK_MODEL_KMEANS_KINDS = "hjqsrs.spark.model.kmeans.kinds"
  val HJQSRS_SPARK_MODEL_KMEANS_ITERATION = "hjqsrs.spark.model.kmeans.iteration"
  val HJQSRS_SPARK_MODEL_KMEANS_RUNS = "hjqsrs.spark.model.kmeans.runs"
  val HJQSRS_SPARK_MODEL_KMEANS_MODE = "hjqsrs.spark.model.kmeans.mode"

  val HJQSRS_HBASE_RATINGS_TABLE = "hjqsrs.hbase.ratings.table"
  val HJQSRS_HBASE_RATINGS_COLFAMILY_TEST = "hjqsrs.hbase.ratings.colfamily.test"
  val HJQSRS_HBASE_RATINGS_COLFAMILY_TRAIN = "hjqsrs.hbase.ratings.colfamily.train"
  val HJQSRS_HBASE_RATINGS_COLFAMILY_RESULT = "hjqsrs.hbase.ratings.colfamily.result"
  val HJQSRS_HBASE_RATINGS_QUALIFIER_ALL = "hjqsrs.hbase.ratings.qualifier.all"

  val HJQSRS_HBASE_USERS_TABLE = "hjqsrs.hbase.users.table"
  val HJQSRS_HBASE_USERS_COLFAMILY = "hjqsrs.hbase.users.colfamily"
  val HJQSRS_HBASE_USERS_INFO_ID = "hjqsrs.hbase.users.info.id"

  val HJQSRS_HBASE_MOVIES_TABLE = "hjqsrs.hbase.movies.table"
  val HJQSRS_HBASE_MOVIES_COLFAMILY = "hjqsrs.hbase.movies.colfamily"
  val HJQSRS_HBASE_MOVIES_INFO_ID = "hjqsrs.hbase.movies.info.id"
  val HJQSRS_HBASE_MOVIES_INFO_NAME = "hjqsrs.hbase.movies.info.name"
  val HJQSRS_HBASE_MOVIES_INFO_GENRES = "hjqsrs.hbase.movies.info.genres"

  val HJQSRS_TOPIC = "hjqsrs.topic"
}
