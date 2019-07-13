package data

import Seller
import kotlin.random.Random
import kotlin.random.nextInt

internal object SellersData {

    val sellers: List<Seller> = generateSellers()
        get() {
            repeat(4) { println("fetching sellers on thread ${Thread.currentThread()} "); Thread.sleep(700) }
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
                OffersData.fastOffers.let { offers ->
                    val random = Random.nextInt(0 until offers.size)
                    offers[random].id
                }
            }.take(Random.nextInt(0 until 10))
                .distinct()
                .toList()
            Seller(name, sellerOffers)
        }
    }
}