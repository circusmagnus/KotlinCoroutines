import data.SellersData
import java.io.IOException

class SellersRepository {

    fun getSellersBlocking(): List<Seller> {
        throw IOException("Sellers are on strike")
        return SellersData.sellers
    }
}