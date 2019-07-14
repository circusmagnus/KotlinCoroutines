import kotlinx.coroutines.cancel

fun main() {
    val playground = Playground(BlockingOffersRepository(), SellersRepository(), ConsoleDisplay())
    Thread.sleep(500)
    playground.startAnimation()
    playground.showOffersWithQuery("Krzesło")
    Thread.sleep(500)
    playground.showSellersWithOffer("Krzesło")
    Thread.sleep(1100)
    playground.cancel()
}