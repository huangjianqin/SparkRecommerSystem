import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by 健勤 on 2017/3/1.
 */
object SparkHbase {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("spark-hbase")
      .setMaster("spark://ubuntu:7077")
      .setJars(Array("E:\\javawebapps\\SparkRecommerSystem\\out\\artifacts\\sparkrecommersystem_jar\\sparkrecommersystem.jar"))
//          .setMaster("local[5]")
    val sc = new SparkContext(conf)

    val data = Range(0, 100)
    val a = Map("0" -> 100)
    val dataRDD = sc.parallelize(data).map(_ + a("0"))
    val num = dataRDD.count()
    println("数量有" + num + "个>>>>>")
    dataRDD.foreach(println)

    //从HBase读取内容
//    val hbaseConf = HBaseConfiguration.create()
//    hbaseConf.set(HConstants.ZOOKEEPER_QUORUM, "192.168.40.128")
//    hbaseConf.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
//    hbaseConf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, "/hbase")
//    hbaseConf.set(TableInputFormat.INPUT_TABLE, "mytable")
//
//    val data = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
//      .map(_._2).map{result =>
//      var list = List[Tuple3[String, String, String]]()
//      for(cell <- result.rawCells()){
//        val row = new String(CellUtil.cloneRow(cell))
//        val colfamily =  new String(CellUtil.cloneFamily(cell))
//        val qualifier =  new String(CellUtil.cloneQualifier(cell))
//        list = list :+ (row, colfamily, qualifier)
//      }
//      (1 ,list)
//    }.reduceByKey(_ ++: _).map(_._2).foreach(println)

//    //往HBase写内容
//    val hbaseConf = HBaseConfiguration.create()
//    hbaseConf.set(HConstants.ZOOKEEPER_QUORUM, "192.168.40.128")
//    hbaseConf.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
//    hbaseConf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, "/hbase")
//    hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, "mytable")
//
//    val job = Job.getInstance(hbaseConf)
////    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
////    job.setOutputValueClass(classOf[Put])
//    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
//
//    val tuples = Array(("forth", "cf", "spark", "5"), ("fifth", "cf", "spark", "6"), ("jully", "cf", "spark", "7"))
//
//    val rdd = sc.parallelize(tuples).map{case (row, colFamily, qualifier, value) =>
//        val put = new Put(Bytes.toBytes(row))
//        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(qualifier), Bytes.toBytes(value))
//        (new ImmutableBytesWritable(), put)
//    }
//
//    rdd.saveAsNewAPIHadoopDataset(job.getConfiguration)
    sc.stop()

  }
}
