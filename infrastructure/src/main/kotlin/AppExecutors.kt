import java.util.concurrent.Executors

internal object AppExecutors {

    val fiveThreadsPool = Executors.newFixedThreadPool(5)
}