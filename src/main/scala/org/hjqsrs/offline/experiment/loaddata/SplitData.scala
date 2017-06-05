package org.hjqsrs.offline.experiment.loaddata

import java.io.{File, PrintWriter}

import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by 健勤 on 2017/1/4.
 */
object SplitData {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("splitData").setMaster("local")
    val sc = new SparkContext(conf)

    val dataSource = sc.textFile("ml-latest/ratings.csv").filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*"))
    val splits = dataSource.randomSplit(Array(0.8, 0.2))

    val file1 = new File("ml-data/data.csv")
    val file2 = new File("ml-data/test.csv")

    if(file1.exists()){
      file1.delete()
    }
    file1.createNewFile()

    if(file2.exists()){
      file2.delete()
    }
    file2.createNewFile()

    val pw1 = new PrintWriter("ml-data/data.csv")
    val pw2 = new PrintWriter("ml-data/test.csv")

    splits(0).toLocalIterator.foreach(pw1.println)
    splits(1).toLocalIterator.foreach(pw2.println)

    pw1.close()
    pw2.close()

    //测试数据集切分后的大小是否正确
    val count11 = splits(0).count()
    val count12 = splits(1).count()

    val count21 = sc.textFile("ml-data/data.csv").count()
    val count22 = sc.textFile("ml-data/test.csv").count()

    sc.stop()

    println("RDD sum:" + (count11 + count12))
    println(count11)
    println(count12)

    println("reload file sum:" + (count21 + count22))
    println(count21)
    println(count22)


  }
}
