import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlin.coroutines.CoroutineContext

class Playground(
    private val offersRepository: OffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

    val displayActor: SendChannel<String> = TODO()

    fun startAnimation() {
        launch { runDotAnim() }
    }

    fun showOffersWithQuery(query: String) {
        launch {
            val offers = getOffers(query)
            display.showNewLine("Done. Offers: $offers")
        }
    }

    private suspend fun runDotAnim() = coroutineScope {
        while (isActive) {
            delay(200); display.showNewLine(".")
        }
    }

    fun showSellersWithOffer(offerQuery: String) {
        launch {
            val sellers = getSellersForOffer(offerQuery)
            display.showNewLine("Done. Sellers: $sellers")
        }
    }

    private suspend fun getOffers(query: String): List<Offer> = withContext(Dispatchers.IO) {
        offersRepository.getOffersBlocking(query)
    }

    private suspend fun getSellers() = withContext(Dispatchers.IO) {
        sellersRepository.getSellersBlocking()
    }

    private suspend fun getSellersForOffer(offerQuery: String): List<Seller> = coroutineScope {
        val getOffers = async { getOffers(offerQuery) }
        val getSellers = async { getSellers() }

        getSellers.await().filterSellingOffers(getOffers.await())
    }

    private fun List<Seller>.filterSellingOffers(offers: List<Offer>) = filter { seller ->
        offers.any { offer -> seller.offerIds.contains(offer.id) }
    }

    fun showSortedOffers(queries: List<String>) {
        runBlocking {

            val queriesProducer = produce(capacity = 4) { queries.forEach { send(it) } }

            val unsortedOffersProducer = produce(capacity = 4) {
                repeat(4) {
                    launch(Dispatchers.IO) {
                        for (query in queriesProducer) send(getOffers(query))
                    }
                }
            }

            val sortedOffersProducer = produce(capacity = 4) {
                repeat(4) {
                    launch(Dispatchers.Default) {
                        for (unsorted in unsortedOffersProducer) send(unsorted.sorted())
                    }
                }
            }

            launch {
                sortedOffersProducer.consumeEach { sorted -> sorted.forEach { display.showNewLine(it.toString()) } }
            }
        }
    }
}