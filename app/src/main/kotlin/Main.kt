fun main(){
    repeat(3) { Thread.sleep(200); println(".") }
    val result = OffersRepository().getOffers("Krzesło")
    println("Done. Offers: $result")
    repeat(3) { Thread.sleep(200); println(".") }
}