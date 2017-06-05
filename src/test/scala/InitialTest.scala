import org.hjqsrs.common.RSContext
import org.hjqsrs.offline.util.{InitEnviroment, LoadInfo, SplitData}

/**
 * Created by 健勤 on 2017/3/8.
 */
object InitialTest {
  def main(args: Array[String]) {
    RSContext()
    InitEnviroment.initEnviroment()
    RSContext.context.destroy()
  }
}
