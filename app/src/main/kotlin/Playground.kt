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

//        suspendCoroutine { coroutine ->
//        offersRepository.getOffersAsync(query) { offers ->
//            coroutine.resume(offers)
//        }
//    }

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
            //            queries.map { query ->
//                    launch {
//                        val offers = getOffers(query)
//                        val sortedOffers = withContext(Dispatchers.Default) { insertionSort(offers.toTypedArray()) }
//                        sortedOffers.forEach { display.showNewLine(it.toString()) }
//                    }
//                }

            val queriesChannel = Channel<String>(4)
            val unsortedOffersChannel = Channel<List<Offer>>(4)
            val sortedOffersChannel = Channel<List<Offer>>(4)

            launch {
                queries.forEach { queriesChannel.send(it) }
                queriesChannel.close()
            }


            repeat(4) {
                launch(Dispatchers.IO) {
                    queriesChannel.consumeEach { query -> unsortedOffersChannel.send(getOffers(query)) }
                    unsortedOffersChannel.close()
                }
            }

            repeat(4) {
                launch(Dispatchers.Default) {
                    unsortedOffersChannel.consumeEach { unsorted ->
                        val sorted = unsorted.sorted()
                        sortedOffersChannel.send(sorted)
                    }
                    sortedOffersChannel.close()
                }
            }
            launch { sortedOffersChannel.consumeEach { sorted -> sorted.forEach { display.showNewLine(it.toString()) } } }


        }
    }
}