import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        //        val appScope = this + Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        val playground = Playground(OffersRepository(), SellersRepository(), ConsoleDisplay(), this)

        playground.startAnimation()
        playground.showSellersForOffers(listOf("Krzesło", "Telefon", "Rower"))
        delay(5_000)
        playground.cancel()
    }
}