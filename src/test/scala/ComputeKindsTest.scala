import org.hjqsrs.common.RSContext
import org.hjqsrs.offline.clustering.ComputeKinds

/**
 * Created by 健勤 on 2017/8/1.
 */
object ComputeKindsTest {
  def main(args: Array[String]) {
    //加载系统上下文,包括加载配置,加载spark context
    val rsContext = RSContext()
    //加载训练数据集
    rsContext.initData()
    //对所有电影聚类
    ComputeKinds.computeSimilarity(rsContext)
  }
}
