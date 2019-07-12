import kotlin.random.Random
import kotlin.random.nextInt

class OffersRepository {

    fun getOffers(query: String): List<Offer> {
        return OffersData.offers.filter { offer -> offer.name == query }
    }
}

private object OffersData {

    val offers: List<Offer> = generateOffers()
        get() {
            Thread.sleep(10 * 1000)
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

class SellersRepository {

    fun getUsers(query: String): List<Seller> {
        return SellersData.sellers
    }
}

private object SellersData {

    val sellers: List<Seller> = generateSellers()
        get() {
            Thread.sleep(500)
            return field
        }

    private fun generateSellers(): List<Seller> {
        val names = listOf(
            "Julian",
            "Marianna",
            "Roman",
            "Stefan",
            "Diomedes",
            "Temistokles",
            "Twardosław",
            "Ryk",
            "Praksewa",
            "Witgunda"
        )

        return names.map { name ->
            val sellerOffers = generateSequence {
                OffersData.offers.let { offers ->
                    val random = Random.nextInt(0 until offers.size)
                    offers[random].id
                }
            }.toList()
            Seller(name, sellerOffers)
        }
    }
}