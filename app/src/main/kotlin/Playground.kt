import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
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

            launch {
                queries.forEach { queriesChannel.send(it) }
                queriesChannel.close()
            }

            launch {
                coroutineScope {
                    repeat(4) {
                        launch {
                            for (query in queriesChannel) unsortedOffersChannel.send(getOffers(query))
                        }
                    }
                }
                unsortedOffersChannel.close()
            }

            repeat(4) {
                launch {
                    for (unsorted in unsortedOffersChannel) {
                        unsorted.sorted().forEach { sorted -> display.showNewLine(sorted.toString()) }
                    }
                }
            }
        }
    }

    private fun CoroutineScope.produceOffersFromDb(queriesChannel: ReceiveChannel<String>) = produce {
        repeat(4) {
            launch { for (query in queriesChannel) send(getOffers(query)) }
        }
    }
}