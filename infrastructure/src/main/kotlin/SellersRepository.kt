import data.SellersData

class SellersRepository {

    fun getUsers(query: String): List<Seller> {
        return SellersData.sellers.filter { it.name == query }
    }
}