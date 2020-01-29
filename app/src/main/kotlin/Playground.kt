import kotlinx.coroutines.*
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Playground(
    private val offersRepository: OffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) : CoroutineScope by CoroutineScope(
    Executors.newSingleThreadExecutor().asCoroutineDispatcher() +
            SupervisorJob() +
            CoroutineExceptionHandler { _, e -> display.showNewLine("Fail: $e") }
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

    private suspend fun getOffers(query: String) = suspendCoroutine<List<Offer>> { cont ->
        offersRepository.getOffersAsync(query)
            .thenAccept { offers -> cont.resume(offers) }
    }

    private suspend fun getSellers() = suspendCoroutine<List<Seller>> { cont ->
        sellersRepository.getSellersAsync()
            .thenAccept { cont.resume(it) }
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