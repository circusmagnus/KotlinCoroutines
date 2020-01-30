import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume

class Playground(
    private val offersRepository: OffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display,
    scope: CoroutineScope
) : CoroutineScope by scope {

    fun startAnimation() {
        launch { runDotAnim() }
    }

    private suspend fun runDotAnim() {
        while (true) {
            delay(200); display.showNewLine(".")
        }
    }

    fun showSellersForOffers(queries: List<String>) {
        launch {
            getQueriesFlow(queries)
                .mapToQueryWithOffers()
                .mapToQueryWithSellers()
                .collect { sellersForQuery -> }
        }
    }

    private fun getQueriesFlow(queries: List<String>): Flow<String> = TODO()

    private fun Flow<String>.mapToQueryWithOffers(): Flow<Pair<String, List<Offer>>> = TODO()

    private fun Flow<Pair<String, List<Offer>>>.mapToQueryWithSellers(): Flow<Pair<String, List<Seller>>> = TODO()

    private suspend fun getOffers(query: String) = suspendCancellableCoroutine<List<Offer>> { cont ->
        offersRepository.getOffersAsync(query)
            .thenAccept { offers -> cont.resume(offers) }
    }

    private suspend fun getSellers() = suspendCancellableCoroutine<List<Seller>> { cont ->
        sellersRepository.getSellersAsync()
            .thenAccept { cont.resume(it) }
    }

    private suspend fun getSellersForOffer(offerQuery: String): List<Seller> = coroutineScope {
        val getOffers = async { getOffers(offerQuery) }
        val getSellers = async { getSellers() }

        val (offers, sellers) = Pair(getOffers.await(), getSellers.await())
        sellers.filterSellingOffers(offers)
    }

    private fun List<Seller>.filterSellingOffers(offers: List<Offer>) = filter { seller ->
        offers.any { offer -> seller.offerIds.contains(offer.id) }
    }
}