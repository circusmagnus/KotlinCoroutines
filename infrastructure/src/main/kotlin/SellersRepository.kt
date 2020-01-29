import data.SellersData
import java.util.concurrent.CompletableFuture

class SellersRepository {

    fun getSellersBlocking(): List<Seller> {
        return SellersData.sellers
    }

    fun getSellersAsync(): CompletableFuture<List<Seller>> = CompletableFuture.supplyAsync {
        SellersData.sellers
    }
}