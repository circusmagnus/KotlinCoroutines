import kotlinx.coroutines.runBlocking

fun main(){
    repeat(3){ Thread.sleep(200) ; print(".") }
    OffersRepository().getOffers("Krzesło")
    print("done")
    repeat(3){ Thread.sleep(200) ; print(".") }

}