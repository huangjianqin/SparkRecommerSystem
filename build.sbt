name := "SparkRecommerSystem"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.6.2",
  "org.apache.spark" % "spark-sql_2.10" % "1.6.2",
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.2",
  "org.apache.spark" % "spark-mllib_2.10" % "1.6.2",
//spark mlib依赖于breeze优化数学运算,而breeze又依赖于下包
  "com.github.fommil.netlib" % "all" % "1.1.2",
  "org.apache.hadoop" % "hadoop-client" % "2.6.4",
//不需要
//  "org.apache.hadoop" % "hadoop-common" % "2.6.4",
//  "org.apache.hadoop" % "hadoop-hdfs" % "2.6.4",
//  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.6.4" ,
//  "org.apache.hadoop" % "hadoop-annotations" % "2.6.4",
  "org.apache.kafka" % "kafka-clients" % "0.10.0.1",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.2",
  "mysql" % "mysql-connector-java" % "5.1.38",
  //也是jackson包冲突,包内的类各种覆盖重写
  //  "org.apache.kafka" % "kafka-streams" % "0.10.0.1",
  "org.apache.zookeeper" % "zookeeper" % "3.4.9",
  "org.apache.hbase" % "hbase-client" % "1.2.4",
//好像是下载不了的
//  "org.apache.hbase" % "hbase" % "1.2.4",
  "org.apache.hbase" % "hbase-common" % "1.2.4",
  "org.apache.hbase" % "hbase-server" % "1.2.4",
  //提交Hadoop与HBase兼容包
  "org.apache.hbase" % "hbase-hadoop-compat" % "1.2.4",
  //hive依赖包
  "org.apache.hive" % "hive-common" % "1.2.1",
  "org.apache.hive" % "hive-jdbc" % "1.2.1",
  "org.apache.hive" % "hive-serde" % "1.2.1",
  "org.apache.hive" % "hive-metastore" % "1.2.1",
  "log4j" % "log4j" % "1.2.17",
  //与scala自带的josn冲突
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
  //与下面的各种包相互依赖之间又有版本冲突
).map(
  _.excludeAll(ExclusionRule(organization = "org.mortbay.jetty"))
).map(
  _.excludeAll(ExclusionRule(organization = "javax.servlet"))
)


    