fun main(){
    val playground = Playground(BlockingOffersRepository(), SellersRepository(), ConsoleDisplay())
    playground.showSellersWithOffer("Krzesło")
}