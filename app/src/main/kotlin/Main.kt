fun main() {
    val playground = Playground(BlockingOffersRepository(), SellersRepository(), ConsoleDisplay())
    Thread.sleep(500)
    playground.startAnimation()
    playground.showOffersForQuery("Krzesło")
    Thread.sleep(500)
    playground.showSellersWithOffer("Krzesło")
    Thread.sleep(20_000)
    playground.cancel()
}