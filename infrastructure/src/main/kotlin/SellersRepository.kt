import data.SellersData
import java.util.concurrent.Callable
import java.util.concurrent.Future

class SellersRepository {

    fun getSellersBlocking(): List<Seller> {
        return SellersData.sellers
    }

    fun getSellersAsync(): Future<List<Seller>> = AppExecutors.fiveThreadsPool.submit(Callable {
        SellersData.sellers
    })
}