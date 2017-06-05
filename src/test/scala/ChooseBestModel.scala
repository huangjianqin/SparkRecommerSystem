import org.hjqsrs.common.RSContext
import org.hjqsrs.model.als.ALSModel

/**
 * Created by 健勤 on 2017/3/18.
 */
object ChooseBestModel {
  def main(args: Array[String]) {
    RSContext()
    RSContext.context.initData()
    val param = Array((50, 550, 50), (10, 60, 10), (1, 11, 1))
    ALSModel.chooseBestModel(RSContext.context.getRatingsRDD, param)
    RSContext.context.destroy()

  }
}
