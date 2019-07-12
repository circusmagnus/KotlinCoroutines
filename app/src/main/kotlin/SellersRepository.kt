//class data.SellersRepository {
//
//    fun getUsers(query: String): Collection<data.Offer> {
//        Thread.sleep(500)
//        return data.SellersData.sellers.filterValues { offer -> offer.name == query }.values
//    }
//}
//
//private object data.SellersData {
//
//    val sellers: Map<String, data.Seller> = generateOffers()
//
//    private fun generateSellers(): Map<String, data.Seller>{
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
//        return ids.mapIndexed { idIndex, id -> data.Offer(id, names[idIndex % names.size]) }
//            .map { it.id to it }
//            .toMap()
//    }
//}