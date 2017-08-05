import org.apache.spark.mllib.clustering.KMeansModel
import org.hjqsrs.common.{RSConstants, RSContext}
import org.hjqsrs.offline.clustering.CoolRecommend

/**
 * Created by 健勤 on 2017/8/1.
 */
object CoolRecommendTest {
  def main(args: Array[String]) {
    //加载系统上下文,包括加载配置,加载spark context
    val rsContext = RSContext()
    //加载训练数据集
    rsContext.initData()
    //根据用户选中喜欢的电影进行推荐
    val recommender = new CoolRecommend(KMeansModel.load(rsContext.sparkContext, RSContext.context(RSConstants.HJQSRS_SPARK_MODEL_KMEANS)))

  }
}
