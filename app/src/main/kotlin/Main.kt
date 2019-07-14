import kotlinx.coroutines.cancel

fun main() {
    val playground = Playground(OffersRepository(), SellersRepository(), ConsoleDisplay())
    Thread.sleep(500)
    playground.showRebates("Krzesło")

    playground.startAnimation()
    playground.showOffersWithQuery("Krzesło")
    Thread.sleep(500)
    playground.showSellersWithOffer("Krzesło")
    Thread.sleep(500)
    Thread.sleep(10_000)
    playground.cancel()
}