import data.OffersData
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class OffersRepository {

    fun getOffersBlocking(query: String): List<Offer> {
        return OffersData.offers.filter { offer -> offer.name.contains(query, true) }
    }

    fun getOffersAsync(query: String): Future<List<Offer>> = CompletableFuture.supplyAsync {
        OffersData.offers
            .filter { offer -> offer.name.contains(query, true) }
    }
}

