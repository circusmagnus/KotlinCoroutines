fun main(){
    repeat(3){ Thread.sleep(200) ; print(".") }
    OffersRepository().getOffers("Krzesło").let { print("done") }
    repeat(3){ Thread.sleep(200) ; print(".") }

}