import kotlinx.coroutines.*

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) : CoroutineScope by CoroutineScope(Dispatchers.Default) {

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

    private suspend fun getOffers(query: String) = withContext(Dispatchers.IO) {
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
}