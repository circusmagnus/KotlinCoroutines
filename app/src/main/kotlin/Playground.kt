import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Playground(
    private val offersRepository: OffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()


    fun showSortedOffers(queries: List<String>) {
        runBlocking {
            queries
                .map { query ->
                    launch {
                        val offers = getOffers(query)
                        val sorted = offers.sorted()
                        sorted.forEach { display.showNewLine(it.toString()) }
                    }
                }
        }
    }

    private suspend fun getOffers(query: String): List<Offer> = withContext(Dispatchers.Default) {
        offersRepository.getOffersBlocking(query)
    }

    fun startAnimation() {
        launch { runDotAnim() }
    }

    private suspend fun runDotAnim() = coroutineScope {
        while (isActive) {
            delay(200); display.showNewLine(".")
        }
    }
}