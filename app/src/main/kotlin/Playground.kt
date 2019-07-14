import kotlinx.coroutines.*
import java.io.IOException

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) : CoroutineScope by CoroutineScope(
    Dispatchers.Default + SupervisorJob() + CoroutineExceptionHandler { _, e -> display.showNewLine("Fail: $e") }
) {

    fun startAnimation() {
        launch { runDotAnim() }
    }

    fun showOffersWithQuery(query: String) {
        launch {
            val offers = getOffers(query)
            display.showNewLine("Done. Offers: $offers")
        }
    }

    private suspend fun runDotAnim() {
        while (true) {
            delay(200); display.showNewLine(".")
        }
    }

    fun showSellersWithOffer(offerQuery: String) {
        launch {
            val sellers = retryIO(3) { getSellersForOffer(offerQuery) }
            display.showNewLine("Done. Sellers: $sellers")
        }
    }

    private suspend fun getOffers(query: String) = withContext(Dispatchers.IO) {
        offersRepository.getOffersBlocking(query)
    }

    suspend fun getSellers() = withContext(Dispatchers.IO) {
        sellersRepository.getSellersBlocking()
    }

    private suspend fun getSellersForOffer(offerQuery: String): List<Seller> = coroutineScope {
        val getOffers = async { getOffers(offerQuery) }
        val getSellers = async { getSellers() }

        val (offers, sellers) = Pair(getOffers.await(), getSellers.await())
        sellers.filterSellingOffers(offers)
    }

    private suspend fun <T> retryIO(times: Int, what: suspend () -> T): T? {

        suspend fun retry(retryCount: Int): T? = try {
            what()
        } catch (e: IOException) {
            if (retryCount < times) {
                display.showNewLine("failed to load data, retry count: $retryCount")
                retry(retryCount + 1)
            } else {
                display.showNewLine("failed to load data, max retries reached")
                null
            }
        }

        return retry(0)
    }


    private fun List<Seller>.filterSellingOffers(offers: List<Offer>) = filter { seller ->
        offers.any { offer -> seller.offerIds.contains(offer.id) }
    }
}