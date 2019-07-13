import data.OffersData

class BlockingOffersRepository {

    fun getOffersBlocking(query: String): List<Offer> {
        return OffersData.offers.filter { offer -> offer.name == query }
    }
}

