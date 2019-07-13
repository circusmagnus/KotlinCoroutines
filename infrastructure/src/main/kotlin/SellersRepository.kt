import data.SellersData

class SellersRepository {

    fun getSellersBlocking(): List<Seller> {
        return SellersData.sellers
    }
}