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

    fun showOffersWithQuery(query: String) {
        launch {
            val offers = getOffers(query)
            display.showNewLine("Done. Offers: $offers")
        }
    }

    fun showSellersWithOffer(offerQuery: String) {
        launch {
            val sellers = getSellersForOffer(offerQuery)
            display.showNewLine("Done. Sellers: $sellers")
        }
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
}