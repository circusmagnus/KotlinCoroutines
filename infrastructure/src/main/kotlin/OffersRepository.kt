import data.OffersData

class OffersRepository {

    fun getOffersBlocking(query: String): List<Offer> {
        return OffersData.offers.filter { offer -> offer.name.contains(query, true) }
    }

    fun getOffersAsync(query: String, whenDone: (List<Offer>) -> Unit) {
        Thread {
            val offers = OffersData.offers.filter { offer -> offer.name.contains(query, true) }
            whenDone(offers)
        }.start()
    }

    fun getOffersFast(query: String): List<Offer> {
        return OffersData.fastOffers.filter { offer -> offer.name.contains(query, true) }
    }
}

