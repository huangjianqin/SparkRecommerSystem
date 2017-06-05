import org.hjqsrs.common.RSContext
import org.hjqsrs.model.als.ALSModel

/**
 * Created by 健勤 on 2017/3/9.
 */
object ALSModelTest {
  def main(args: Array[String]) {
    RSContext()
    RSContext.context.initData()
    ALSModel.train(RSContext.context.sparkContext, RSContext.context.getRatingsRDD)
    RSContext.context.destroy()
    Thread.sleep(5000)
  }
}
