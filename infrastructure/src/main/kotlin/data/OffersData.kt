package data

import Offer

internal object OffersData {

    val fastOffers = generateOffers()

    val offers: List<Offer> = fastOffers
        get() {
            return field
        }

    private fun generateOffers(): List<Offer> {
        val ids = generateSequence(1) { id -> id + 1 }
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
    }
}