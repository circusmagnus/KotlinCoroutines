import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

class Playground(
    private val offersRepository: OffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

    fun startAnimation() {
        launch { runDotAnim() }
    }

    private suspend fun runDotAnim() = coroutineScope {
        while (isActive) {
            delay(200); display.showNewLine(".")
        }
    }

    private suspend fun getOffers(query: String): List<Offer> = withContext(Dispatchers.IO) {
        offersRepository.getOffersBlocking(query)
    }

    fun showSortedOffers(queries: List<String>) {
        runBlocking {

            val queriesChannel = Channel<String>()
            val unsortedOffersChannel = Channel<List<Offer>>()
            val sortedOffersChannel = Channel<List<Offer>>()

            launch {
                queries.forEach { queriesChannel.send(it) }
                queriesChannel.close()
            }

            launch {
                coroutineScope {
                    repeat(4) {
                        launch(Dispatchers.IO) {
                            for (query in queriesChannel) unsortedOffersChannel.send(getOffers(query))
                        }
                    }
                }
                unsortedOffersChannel.close()
            }

            launch {
                coroutineScope {
                    repeat(4) {
                        launch(Dispatchers.Default) {
                            for (unsorted in unsortedOffersChannel) sortedOffersChannel.send(unsorted.sorted())
                        }
                    }
                }
                sortedOffersChannel.close()
            }
            launch {
                var offersShown = 0
                sortedOffersChannel.consumeEach { sorted ->
                    sorted.forEach {
                        display.showNewLine(it.toString())
                        display.showNewLine("It is ${++offersShown} offer")
                        delay(200)
                    }
                }
            }
        }
    }
}