import java.util.Properties
import java.util.concurrent.TimeUnit

import org.apache.kafka.clients.producer._
import org.apache.log4j.Logger

import scala.io.Source

/**
 * Created by 健勤 on 2017/3/13.
 */
object KafkaRatingProducer {
  private val log = Logger.getLogger(KafkaRatingProducer.getClass)
  def main(args: Array[String]) {
//    val ratingArr = getTestRatings("test/testRatings.csv", 6, 10)
    val ratingArr = Array("1::1161::4.0")

    val properties = new Properties()
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ubuntu:9092")
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    val topics = Set("ratings")
    val producer = new KafkaProducer[String, String](properties)

    for(ratings <- ratingArr) {
      producer.send(new ProducerRecord[String, String]("ratings", null, ratings), new Callback {
        override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
          log.info("send rating msg => " + recordMetadata.toString)
        }
      })
    }

    producer.close(10, TimeUnit.SECONDS)
  }

  def getTestRatings(path: String, start: Int, end: Int): Array[String] ={
    val file = Source.fromFile(path)
    return file.getLines().slice(start, end).map{line =>
      val splits = line.split(",")
      (splits(0).toInt, splits(1).toInt, splits(2).toDouble)
    }.filter{case (user, movie, rating) =>
      user > 100 || movie > 100
    }.map{case (user, movie, rating) =>
      user + "::" + movie + "::" + rating
    }.toArray
  }
}
