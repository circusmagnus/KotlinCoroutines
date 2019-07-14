import kotlinx.coroutines.*

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val sellersRepository: SellersRepository,
    private val display: Display
) {

    fun getOffersWithQuery(query: String) {
        runBlocking {
            val fetchOffers = async(Dispatchers.IO) { offersRepository.getOffersBlocking(query) }

            val anim = launch {
                while (isActive) {
                    delay(200); display.showNewLine(".")
                }
            }
            val offers = fetchOffers.await()
            anim.cancelAndJoin()
            display.showNewLine("Done. Offers: $offers")
        }
    }

    fun getSellersForMultiQuery(items: List<String>) {
        runBlocking {

        }

    }

    suspend fun getOffers(query: String) = withContext(Dispatchers.IO) {
        offersRepository.getOffersBlocking(query)
    }

    suspend fun getSellers() = withContext(Dispatchers.IO) {
        sellersRepository.getSellersBlocking()
    }

    suspend fun getSellersWithOffers(forQueries: List<String>) = coroutineScope {
        val offers = async { getOffers() }
    }
}