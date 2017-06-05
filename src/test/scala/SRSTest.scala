import org.hjqsrs.common.{RSConstants, RSContext}
import org.hjqsrs.model.als.ALSModel
import org.hjqsrs.online.realtime.KafkaCollector
import org.hjqsrs.online.rs.WeightedHybridRS

/**
 * Created by 健勤 on 2017/3/12.
 */
object SRSTest {
  def main(args: Array[String]) {
    //加载系统上下文,包括加载配置,加载spark context
    val rsContext = RSContext()
    //加载训练数据集
    rsContext.initData()
    //构建kafka与spark streaming结合的rating流实时收集类
    val alsModel = ALSModel(rsContext.sparkContext, RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_ALS))
//    val alsModel = ALSModel.train(rsContext.sparkContext, rsContext.getRatingsRDD)
    val weightedHybridRS = WeightedHybridRS(List((alsModel, 1.0)))
    val kafkaCollector = KafkaCollector(weightedHybridRS)
    //首先初始化对没有评分过的movie的评分
//    WeightedHybridRS.initialPredict(weightedHybridRS)
    //运行实时流,实时收集记录到kafka的rating信息
    kafkaCollector.run()
    //释放资源
    Thread.sleep(10000)
    rsContext.destroy()
  }
}
