import kotlinx.coroutines.*

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) {

    fun showOffersForQuery(query: String) {
        runBlocking {
            val anim = launch {
                while (true) {
                    delay(200); display.showNewLine(".")
                }
            }
            val offers = getOffers()
            anim.cancelAndJoin()
            display.showNewLine("Done. Offers: $offers")
        }
    }

    private suspend fun getOffers() = withContext(Dispatchers.IO) { offersRepository.getOffersBlocking("Krzesło") }

    fun showSellersforOfferQuery(offerQuery: String) {

    }

    private fun List<Seller>.filterSellingOffers(offers: List<Offer>): List<Seller> = filter { seller ->
        offers.any { offer -> seller.offerIds.contains(offer.id) }
    }
}