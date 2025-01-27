package data

import Offer

internal object OffersData {

    val offers: List<Offer> = generateOffers()
        get() {
            repeat(5) { println("fetching offers on thread ${Thread.currentThread()} "); Thread.sleep(1000) }
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