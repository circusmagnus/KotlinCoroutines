fun main(){
    repeat(3) { Thread.sleep(200); println(".") }
    val result = BlockingOffersRepository().getOffersBlocking("Krzesło")
    println("Done. Offers: $result")
    repeat(3) { Thread.sleep(200); println(".") }
}