fun main(){
    val playground = Playground(BlockingOffersRepository(), SellersRepository(), ConsoleDisplay())
    playground.getSellersForMultiQuery(listOf("Krzesło", "Rower"))
}