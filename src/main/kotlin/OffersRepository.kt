class OffersRepository {

    fun getOffers(query: String): Collection<Offer> {
        Thread.sleep(1000)
        return OffersData.offers.filterValues { offer -> offer.name == query }.values
    }
}

private object OffersData {

    val offers: Map<String, Offer> = generateOffers()

    private fun generateOffers(): Map<String, Offer>{
        val ids = generateSequence(1) { id -> id + 1  }
            .take(100)
            .map { it.toString() }
            .toList()

        val names = listOf(
            "Krzesło",
            "Łóżko",
            "Telefon",
            "Rower",
            "Spodnie",
            "Zegarek",
            "Fortepian",
            "Mandarynka",
            "Wół",
            "Fajka"
        )

        return ids.mapIndexed { idIndex, id -> Offer(id, names[idIndex % names.size]) }
            .map { it.id to it }
            .toMap()
    }
}