package data

import Seller
import kotlin.random.Random
import kotlin.random.nextInt

internal object SellersData {

    val sellers: List<Seller> = generateSellers()
        get() {
            print("running on thread ${Thread.currentThread()}")
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