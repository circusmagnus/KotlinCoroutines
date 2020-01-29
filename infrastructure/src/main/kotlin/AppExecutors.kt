import java.util.concurrent.Executors

object AppExecutors {

    val fiveThreadsPool = Executors.newFixedThreadPool(5)
}