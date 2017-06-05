package org.hjqsrs.offline.experiment.collaborativefiltering.sparkbase

import java.io.PrintWriter

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{MatrixEntry, CoordinateMatrix, RowMatrix}
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by 健勤 on 2017/1/6.
 */
object SVD {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SVDBaseCF").setMaster("local[5]")
    val sc = new SparkContext(conf)

    val datasource = sc.textFile("ml-data/data.csv").filter(_.matches("\\d*,\\d*,\\d*\\.\\d*,\\d*"))

    val matrixEntrys = datasource.map(_.split(",")).map{lines =>
      val userId = lines(0).toLong
      val movieId = lines(1).toLong
      val rating = lines(2).toDouble

      new MatrixEntry(userId, movieId, rating)
    }

    val mat = new CoordinateMatrix(matrixEntrys)
    val svd = mat.toIndexedRowMatrix().toRowMatrix().computeSVD(100, true)

    val pwU = new PrintWriter("tempdata/svd-U.txt")
    pwU.println(svd.U.toString)
    pwU.close()

    val pws = new PrintWriter("tempdata/svd-s.txt")
    pws.println(svd.s)
    pws.close()

    val pwV = new PrintWriter("tempdata/svd-V.txt")
    pwV.println(svd.V.toString(mat.numRows().toInt, mat.numCols().toInt))
    pwV.close()

    sc.stop()

  }
}
