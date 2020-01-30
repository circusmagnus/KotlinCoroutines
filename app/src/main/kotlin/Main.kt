import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val playgroundScope = this + Job(parent = coroutineContext[Job])
        val playground = Playground(OffersRepository(), SellersRepository(), ConsoleDisplay(), playgroundScope)

        playground.startAnimation()
        playground.showSellersForOffers(listOf("Krzesło", "Telefon", "Rower"))
        delay(10_000)
        playground.cancel()
    }
}