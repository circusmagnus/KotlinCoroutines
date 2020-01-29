import kotlinx.coroutines.cancel

fun main() {
    val playground = Playground(OffersRepository(), SellersRepository(), ConsoleDisplay())
    Thread.sleep(500)
    playground.startAnimation()
    playground.showOffersWithQuery("Krzesło")
    Thread.sleep(500)
    playground.showSellersWithOffer("Krzesło")
    Thread.sleep(5_000)
    playground.cancel()
    println("canceled!")
    Thread.sleep(5_000)
}