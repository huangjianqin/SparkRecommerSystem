package org.hjqsrs.offline.experiment.collaborativefiltering.sparkbase

import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by 健勤 on 2017/1/10.
 */
object TF_IDF {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("TF-IDF").setMaster("local[5]")
    val sc = new SparkContext(conf)

    val documents = sc.textFile("ml-data/movies.csv").filter(_.matches("\\d*,.*,.*")).map{line =>
      val splits = line.split(",")
      splits(splits.length - 1).split("\\|").toSeq
    }

    val hashingTF = new HashingTF()
    val TF = hashingTF.transform(documents)

    val IDF = new IDF().fit(TF)
    val TFIDF = IDF.transform(TF)

    TFIDF.foreach(println)

    sc.stop()
  }
}
