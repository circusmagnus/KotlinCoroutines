import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
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
                .collect { (query, sellers) -> display.showNewLine("Result for $query is $sellers") }
        }
    }

    private fun getQueriesFlow(queries: List<String>): Flow<String> = queries.asFlow()

    private fun Flow<String>.mapToQueryWithOffers(): Flow<Pair<String, List<Offer>>> = channelFlow {
        val receiveChannel = Channel<String>()
        launch {
            collect { query -> receiveChannel.send(query) }
        }

        repeat(5) {
            launch {
                for (query in receiveChannel) {
                    send(Pair(query, getOffers(query)))
                }
            }
        }
    }

    private fun Flow<Pair<String, List<Offer>>>.mapToQueryWithSellers(): Flow<Pair<String, List<Seller>>> = flow {
        coroutineScope {
            val sellers = async { getSellers() }
            collect { (query, offers) ->
                val sellersForOffer = sellers.await().filterSellingOffers(offers)
                val queryWithSellers = Pair(query, sellersForOffer)
                emit(queryWithSellers)
            }
        }
    }

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