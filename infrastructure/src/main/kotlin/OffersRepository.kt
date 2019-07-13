import data.OffersData

class OffersRepository {

    fun getOffers(query: String): List<Offer> {
        return OffersData.offers.filter { offer -> offer.name == query }
    }
}

