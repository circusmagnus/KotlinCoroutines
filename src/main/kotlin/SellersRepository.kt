//class SellersRepository {
//
//    fun getUsers(query: String): Collection<Offer> {
//        Thread.sleep(500)
//        return SellersData.sellers.filterValues { offer -> offer.name == query }.values
//    }
//}
//
//private object SellersData {
//
//    val sellers: Map<String, Seller> = generateOffers()
//
//    private fun generateSellers(): Map<String, Seller>{
//        val ids = generateSequence(1) { id -> id + 1  }
//            .take(100)
//            .map { it.toString() }
//            .toList()
//
//        val names = listOf(
//            "Krzesło",
//            "Łóżko",
//            "Telefon",
//            "Rower",
//            "Spodnie",
//            "Zegarek",
//            "Fortepian",
//            "Mandarynka",
//            "Wół",
//            "Fajka"
//        )
//
//        return ids.mapIndexed { idIndex, id -> Offer(id, names[idIndex % names.size]) }
//            .map { it.id to it }
//            .toMap()
//    }
//}