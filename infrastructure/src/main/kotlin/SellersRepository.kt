import data.SellersData
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class SellersRepository {

    fun getSellersBlocking(): List<Seller> {
        return SellersData.sellers
    }

    fun getSellersAsync(): Future<List<Seller>> = CompletableFuture.supplyAsync {
        SellersData.sellers
    }
}